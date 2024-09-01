package sanity.nil.webchat.application.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sanity.nil.webchat.application.dto.message.*;
import sanity.nil.webchat.application.interfaces.repository.FileMetadataRepository;
import sanity.nil.webchat.application.interfaces.repository.MemberRepository;
import sanity.nil.webchat.application.interfaces.repository.MessageFileRepository;
import sanity.nil.webchat.application.interfaces.repository.MessageRepository;
import sanity.nil.webchat.application.interfaces.storage.FileStorage;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoBroadcastPayload;
import sanity.nil.webchat.infrastructure.channels.impl.CentrifugoHelper;
import sanity.nil.webchat.infrastructure.db.postgres.model.FileMetadataModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MessageFileModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MessageModel;
import sanity.nil.webchat.infrastructure.db.postgres.projections.MessageDetailedView;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageFacade {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final FileMetadataRepository fileMetadataRepository;
    private final MessageFileRepository messageFileRepository;
    private final CentrifugoHelper centrifugoHelper;
    private final FileStorage fileStorage;

    public PagedChatMessagesDTO getFilteredMessages(MessageFiltersDTO filters) {
        List<MessageDetailedView> messages = messageRepository.getByFilters(filters);
        List<ChatMessageDTO> chatMessages = messages.stream().map(message -> {
            List<String> attachments = new ArrayList<>();
            if (!CollectionUtils.isEmpty(message.getFilePointers())) {
                attachments = message.getFilePointers().stream().map(filePointer -> {
                    return fileStorage.getFileURL(filePointer.getFileID(), filePointer.getFileDir());
                }).toList();
            }
            return new ChatMessageDTO(message.getMessageID(), message.getSenderID(),
                    message.getContent(), message.getReceivedAt(), attachments);
        }).toList();
        int totalPages = messageRepository.countByFilters(filters);
        if (totalPages % filters.limit == 0) {
            totalPages = totalPages / filters.limit;
        } else {
            totalPages = totalPages / filters.limit + 1;
        }
        return new PagedChatMessagesDTO(chatMessages, totalPages, filters.offset+1);
    }

    @Transactional
    public MessageCreatedDTO sendMessage(OnSendMessageDTO messageDTO) {
        UUID newMessageID = UUID.randomUUID();
        ZonedDateTime sentAt = ZonedDateTime.now();
        List<UUID> memberIDs = memberRepository.getAllByChatID(messageDTO.chatID()).stream()
                .map(MemberModel::getMemberID).toList();
        List<String> channels = memberIDs.stream().map(e -> "personal:" + e.toString()).toList();

        MessageModel message = messageRepository.save(newMessageID, messageDTO.chatID(),
                messageDTO.senderID(), messageDTO.content(), sentAt);
        List<AttachmentDTO> attachments = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messageDTO.attachments())) {
            List<FileMetadataModel> files = fileMetadataRepository.findByIds(
                    messageDTO.attachments().stream()
                            .map(UUID::fromString)
                            .toList()
            );
            List<MessageFileModel> messageFiles = files.stream().map(e ->
                    new MessageFileModel(newMessageID, e.getId(), message, e)
            ).toList();
            messageFileRepository.saveAll(messageFiles);
            files.forEach(e -> {
                String url = fileStorage.getFileURL(e.getId().toString(), e.getDirectory());
                attachments.add(new AttachmentDTO(e.getId(), url));
            });
        }

        String idempotencyKey = String.format("message.%s.created", newMessageID);
        centrifugoHelper.broadcast(new CentrifugoBroadcastPayload(channels,
                new MessageCreateDTO(newMessageID, messageDTO.chatID(), messageDTO.senderID(),
                        messageDTO.content(), attachments),
                idempotencyKey)
        ).subscribe(
                res -> {
                    log.info("A message with id {}, successfully handed to centrifugo {}", newMessageID, res);
                },
                err -> {
                    log.error("Error sending a message to centrifugo {}", err.getMessage());
                }
        );
        return new MessageCreatedDTO(newMessageID, sentAt);
    }

    @Transactional
    public void modifyMessage(OnModifyMessageDTO messageDTO) {
        List<UUID> memberIDs = memberRepository.getAllByChatID(messageDTO.chatID()).stream()
                .map(MemberModel::getMemberID).toList();
        List<String> channels = memberIDs.stream().map(e -> "personal:" + e.toString()).toList();

        MessageModel existingMessage = messageRepository.findById(messageDTO.messageID());
        existingMessage.setContent(messageDTO.content());
        messageRepository.save(existingMessage);

        String idempotencyKey = String.format("message.%s.modified", messageDTO.messageID());
        centrifugoHelper.broadcast(new CentrifugoBroadcastPayload(channels,
                new MessageModifyDTO(messageDTO.messageID(), messageDTO.chatID(), existingMessage.getSender().getMemberID(),
                        messageDTO.content()),
                idempotencyKey)
        ).subscribe(
                res -> {
                    log.info("A message with id {}, successfully handed to centrifugo {}", messageDTO.messageID(), res);
                },
                err -> {
                    log.error("Error sending a message to centrifugo {}", err.getMessage());
                }
        );
    }
}
