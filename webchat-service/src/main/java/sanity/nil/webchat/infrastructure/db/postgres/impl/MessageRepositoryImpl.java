package sanity.nil.webchat.infrastructure.db.postgres.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.dto.message.MessageFiltersDTO;
import sanity.nil.webchat.application.interfaces.repository.MessageRepository;
import sanity.nil.webchat.infrastructure.db.postgres.dao.ChatDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.MemberDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.MessageDAO;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MessageModel;
import sanity.nil.webchat.infrastructure.db.postgres.projections.MessageDetailedView;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

    private final MessageDAO messageDAO;
    private final MemberDAO memberDAO;
    private final ChatDAO chatDAO;

    @Override
    public MessageModel save(UUID messageID, UUID chatID, UUID senderID, String content, ZonedDateTime sentAt) {
        Optional<ChatModel> maybeChat = chatDAO.findById(chatID);
        if (maybeChat.isEmpty()) {
            throw new NoSuchElementException("Chat with ID " + chatID + " does not exist");
        }
        Optional<MemberModel> maybeMember = memberDAO.findById(senderID);
        if (maybeMember.isEmpty()) {
            throw new NoSuchElementException("Member with ID " + senderID + " does not exist");
        }
        return messageDAO.save(new MessageModel(messageID, maybeChat.get(), maybeMember.get(), content, sentAt));
    }

    @Override
    public void save(MessageModel message) {
        messageDAO.save(message);
    }

    @Override
    public MessageModel findById(UUID messageID) {
        return messageDAO.findById(messageID).orElseThrow(
                () -> new NoSuchElementException("No message with such id found")
        );
    }

    @Override
    public List<MessageDetailedView> getByFilters(MessageFiltersDTO filtersDTO) {
        Pageable pageable = PageRequest.of(filtersDTO.offset, filtersDTO.limit);
        return messageDAO.findByFilters(filtersDTO.chatID, filtersDTO.message, pageable);
    }

    @Override
    public int countByFilters(MessageFiltersDTO filtersDTO) {
        return messageDAO.countByFilters(filtersDTO.chatID, filtersDTO.message);
    }
}
