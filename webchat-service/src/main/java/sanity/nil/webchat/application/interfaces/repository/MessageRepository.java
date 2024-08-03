package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.application.dto.ChatMessageDTO;
import sanity.nil.webchat.application.dto.MessageFiltersDTO;
import sanity.nil.webchat.infrastructure.db.model.MessageModel;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save(UUID messageID, UUID chatID, UUID senderID, String content, ZonedDateTime sentAt);
    List<ChatMessageDTO> getByFilters(MessageFiltersDTO filtersDTO);
    int countByFilters(MessageFiltersDTO filtersDTO);
}
