package sanity.nil.webchat.infrastructure.db.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import sanity.nil.webchat.application.consts.TokenType;
import sanity.nil.webchat.application.dto.*;
import sanity.nil.webchat.application.interfaces.repository.ChatRepository;
import sanity.nil.webchat.application.interfaces.repository.MemberRepository;
import sanity.nil.webchat.application.interfaces.repository.MessageRepository;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoBroadcastPayload;
import sanity.nil.webchat.infrastructure.channels.impl.CentrifugoHelper;
import sanity.nil.webchat.infrastructure.db.model.MemberModel;
import sanity.nil.webchat.infrastructure.security.TokenHelper;
import sanity.nil.webchat.infrastructure.storage.s3.FileData;
import sanity.nil.webchat.infrastructure.storage.s3.FileStorage;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatFacade {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final CentrifugoHelper centrifugoHelper;
    private final TokenHelper tokenHelper;
    private final FileStorage fileStorage;

    @Transactional
    public void joinChat(JoinChatDTO joinChatDTO) {
        MemberModel joinedMember = memberRepository.getByMemberID(joinChatDTO.memberID())
                .orElseThrow(
                        () -> new NoSuchElementException("No member found with id " + joinChatDTO.memberID())
                );
        chatRepository.addMember(joinChatDTO.memberID(), joinChatDTO.chatID());
        List<UUID> members = memberRepository.getMemberIdsByChatID(joinChatDTO.chatID());
        List<String> channels = members.stream()
                .map(e -> "personal:" + e.toString()).toList();
        memberRepository.addMemberToChat(joinChatDTO.memberID(), joinChatDTO.chatID());
        centrifugoHelper.broadcast(new CentrifugoBroadcastPayload(channels,
                new OnJoinMemberDTO(joinChatDTO.memberID(), joinedMember.getNickname()),
                "member_joined_" + joinChatDTO.memberID())
        ).subscribe(
                res -> {
                    log.info("A user {} joined chat {}, successfully handed to centrifugo {}",
                            joinChatDTO.memberID(), joinChatDTO.chatID(), res);
                }, err -> {
                    log.error("Error sending a join user event to centrifugo {}", err.getMessage());
                }
        );

    }

    @Transactional
    public void leaveChat(LeaveChatDTO leaveChatDTO) {
        MemberModel leftMember = memberRepository.getByMemberID(leaveChatDTO.memberID())
                .orElseThrow(
                        () -> new NoSuchElementException("No member found with id " + leaveChatDTO.memberID())
                );
        List<UUID> members = memberRepository.getMemberIdsByChatID(leaveChatDTO.chatID());
        List<String> channels = members.stream()
                .map(e -> "personal:" + e.toString()).toList();
        memberRepository.removeMemberFromChat(leaveChatDTO.memberID(), leaveChatDTO.chatID());
        centrifugoHelper.broadcast(new CentrifugoBroadcastPayload(channels,
                new OnLeaveMemberDTO(leaveChatDTO.memberID(), leftMember.getNickname()),
                "member_left_" + leaveChatDTO.memberID())
        ).subscribe(
                res -> {
                    log.info("A user {} left chat {}, successfully handed to centrifugo {}",
                            leaveChatDTO.memberID(), leaveChatDTO.chatID(), res);
                }, err -> {
                    log.error("Error sending a leave user event to centrifugo {}", err.getMessage());
                }
        );
    }

    public List<ChatMemberDTO> getChatMembers(UUID chatID) {
        return memberRepository.getAllByChatID(chatID)
                .stream().map(e -> new ChatMemberDTO(e.getMemberID(), e.getNickname()))
                .collect(Collectors.toList());
    }

    public PagedChatMessagesDTO getFilteredMessages(MessageFiltersDTO filters) {
        List<ChatMessageDTO> messages = messageRepository.getByFilters(filters);
        int totalPages = messageRepository.countByFilters(filters);
        if (totalPages % filters.limit == 0) {
            totalPages = totalPages / filters.limit;
        } else {
            totalPages = totalPages / filters.limit + 1;
        }
        return new PagedChatMessagesDTO(messages, totalPages, filters.offset+1);
    }

    @Transactional
    public UUID sendMessage(OnSendMessageDTO messageDTO) {
        UUID newMessageID = UUID.randomUUID();
        List<UUID> memberIDs = memberRepository.getAllByChatID(messageDTO.chatID()).stream()
                .map(MemberModel::getMemberID).toList();
        List<String> channels = memberIDs.stream().map(e -> "personal:" + e.toString()).toList();
        messageRepository.save(newMessageID, messageDTO.chatID(), messageDTO.authorID(), messageDTO.content(), ZonedDateTime.now());
        centrifugoHelper.broadcast(new CentrifugoBroadcastPayload(channels,
                new MessageCreateDTO(newMessageID, messageDTO.chatID(), messageDTO.authorID(), messageDTO.content()),
                "message-" + messageDTO.authorID().toString())
        ).subscribe(
                res -> {
                    log.info("A message with id {}, successfully handed to centrifugo {}", newMessageID, res);
                },
                err -> {
                    log.error("Error sending a message to centrifugo {}", err.getMessage());
                }
        );
        return newMessageID;
    }

    public String getToken(String tokenType, String channel) {
        // "personal:{userID}"
        Map<String, Object> claims = new HashMap<>();
        switch (TokenType.valueOf(tokenType)) {
            case CONNECTION -> {
                // TODO: channel isn't needed here, we should understand which user it is for from IdentityProvider
                log.info(channel.substring(channel.lastIndexOf(":") + 1));
                UUID userID = UUID.fromString(channel.substring(channel.lastIndexOf(":") + 1));
                claims.put("sub", userID.toString());
            }
            case SUBSCRIPTION -> {
                log.info(channel.substring(channel.lastIndexOf(":") + 1));
                UUID userID = UUID.fromString(channel.substring(channel.lastIndexOf(":") + 1));
                claims.put("sub", userID.toString());
                claims.put("channel", channel);
            }
            default -> {
                log.error("Unsupported token type {}", tokenType);
            }
        }

        return tokenHelper.createToken(claims);
    }

    public Mono<UploadedFilesDTO> uploadFiles(Flux<FilePart> files) {
        return files
                .flatMap(filePart -> {
                    log.info("Received file: {}", filePart.filename());
                    return Mono.fromCallable(() -> new FileData(filePart))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(fileData -> fileStorage.saveFile(fileData, filePart.filename(), "videos"));
                })
                .collectList()
                .map(UploadedFilesDTO::new);
    }

    public FileURLDTO fileUrl(String fileID) {
        return new FileURLDTO(fileStorage.getFileURL(fileID, "videos"));
    }

}
