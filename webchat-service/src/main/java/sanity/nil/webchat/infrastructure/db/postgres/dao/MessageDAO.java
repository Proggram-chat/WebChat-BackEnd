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
                    "AND (:message IS NULL OR LOWER(m.content) LIKE LOWER(CONCAT('%', :message, '%'))) " +
                    "ORDER BY m.receivedAt DESC"
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
                    "AND (:message IS NULL OR LOWER(content) LIKE LOWER(CONCAT('%', :message, '%'))) "
    )
    int countByFilters(
            @Param("chatID") UUID chatID,
            @Param("message") String message
    );
}
