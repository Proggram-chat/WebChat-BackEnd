package sanity.nil.webchat.infrastructure.db.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.dto.ChatMessageDTO;
import sanity.nil.webchat.application.dto.MessageFiltersDTO;
import sanity.nil.webchat.application.interfaces.repository.MessageRepository;
import sanity.nil.webchat.infrastructure.db.model.ChatModel;
import sanity.nil.webchat.infrastructure.db.model.MemberModel;
import sanity.nil.webchat.infrastructure.db.model.MessageModel;

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
    public void save(UUID messageID, UUID chatID, UUID senderID, String content, ZonedDateTime sentAt) {
        Optional<ChatModel> maybeChat = chatDAO.findById(chatID);
        if (maybeChat.isEmpty()) {
            throw new NoSuchElementException("Chat with ID " + chatID + " does not exist");
        }
        Optional<MemberModel> maybeMember = memberDAO.findById(senderID);
        if (maybeMember.isEmpty()) {
            throw new NoSuchElementException("Member with ID " + senderID + " does not exist");
        }
        messageDAO.save(new MessageModel(messageID, maybeChat.get(), maybeMember.get(), content, sentAt));
    }

    @Override
    public List<ChatMessageDTO> getByFilters(MessageFiltersDTO filtersDTO) {
        Pageable pageable = PageRequest.of(filtersDTO.offset, filtersDTO.limit);
        return messageDAO.findByFilters(filtersDTO.chatID, filtersDTO.message, pageable);
    }

    @Override
    public int countByFilters(MessageFiltersDTO filtersDTO) {
        return messageDAO.countByFilters(filtersDTO.chatID, filtersDTO.message);
    }
}
