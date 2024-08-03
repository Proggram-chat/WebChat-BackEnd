package sanity.nil.webchat.infrastructure.db.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sanity.nil.webchat.application.consts.PayloadType;
import sanity.nil.webchat.application.consts.TokenType;
import sanity.nil.webchat.application.dto.*;
import sanity.nil.webchat.application.interfaces.repository.ChatRepository;
import sanity.nil.webchat.application.interfaces.repository.MemberRepository;
import sanity.nil.webchat.application.interfaces.repository.MessageRepository;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoBroadcastPayload;
import sanity.nil.webchat.infrastructure.channels.impl.CentrifugoHelper;
import sanity.nil.webchat.infrastructure.db.model.MemberModel;
import sanity.nil.webchat.infrastructure.security.TokenHelper;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    public boolean joinChat(JoinChatDTO joinChatDTO) {
        try {
            return chatRepository.addUser(joinChatDTO.memberID(), joinChatDTO.chatID()) != null;
        } catch (Exception e) {
            return false;
        }
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

    public UUID sendMessage(OnSendMessageDTO messageDTO) {
        UUID newMessageID = UUID.randomUUID();
        List<UUID> memberIDs = memberRepository.getAllByChatID(messageDTO.chatID()).stream()
                .map(MemberModel::getMemberID).toList();
        List<String> channels = memberIDs.stream().map(e -> "personal:" + e.toString()).toList();
        messageRepository.save(newMessageID, messageDTO.chatID(), messageDTO.authorID(), messageDTO.content(), ZonedDateTime.now());
        centrifugoHelper.broadcast(new CentrifugoBroadcastPayload(channels,
                new MessageCreateDTO(PayloadType.MESSAGE_CREATED, newMessageID, messageDTO.chatID(), messageDTO.authorID(), messageDTO.content()),
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
}
