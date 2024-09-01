package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.application.dto.message.MessageFiltersDTO;
import sanity.nil.webchat.infrastructure.db.postgres.model.MessageModel;
import sanity.nil.webchat.infrastructure.db.postgres.projections.MessageDetailedView;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    MessageModel save(UUID messageID, UUID chatID, UUID senderID, String content, ZonedDateTime sentAt);
    void save(MessageModel message);
    MessageModel findById(UUID messageID);
    List<MessageDetailedView> getByFilters(MessageFiltersDTO filtersDTO);
    int countByFilters(MessageFiltersDTO filtersDTO);
}
