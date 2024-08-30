package sanity.nil.webchat.infrastructure.db.postgres.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanity.nil.webchat.application.dto.ChatMessageDTO;
import sanity.nil.webchat.infrastructure.db.postgres.model.MessageModel;

import java.util.List;
import java.util.UUID;

public interface MessageDAO extends JpaRepository<MessageModel, UUID> {

    @Query(
            value = "SELECT new sanity.nil.webchat.application.dto.ChatMessageDTO(m.id, m.sender.memberID, m.content, m.receivedAt) " +
                    "FROM MessageModel m " +
                    "WHERE (:chatID IS NULL OR :chatID = m.chat.chatID) " +
                    "AND (:message IS NULL OR LOWER(CAST(m.content AS text)) LIKE LOWER(CAST(CONCAT('%', :message, '%') AS text))) " +
                    "ORDER BY m.receivedAt ASC"
    )
    List<ChatMessageDTO> findByFilters(
            @Param("chatID") UUID chatID,
            @Param("message") String message,
            Pageable pageable
    );


    @Query(
            value = "SELECT count(*) " +
                    "FROM MessageModel " +
                    "WHERE (:chatID IS NULL OR :chatID = chat.chatID) " +
                    "AND (:message IS NULL OR LOWER(CAST(content AS text)) LIKE LOWER(CAST(CONCAT('%', :message, '%') AS text))) "
    )
    int countByFilters(
            @Param("chatID") UUID chatID,
            @Param("message") String message
    );
}
