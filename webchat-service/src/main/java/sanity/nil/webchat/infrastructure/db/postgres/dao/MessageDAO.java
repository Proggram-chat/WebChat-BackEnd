package sanity.nil.webchat.infrastructure.db.postgres.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanity.nil.webchat.infrastructure.db.postgres.model.MessageModel;
import sanity.nil.webchat.infrastructure.db.postgres.projections.MessageDetailedView;

import java.util.List;
import java.util.UUID;

public interface MessageDAO extends JpaRepository<MessageModel, UUID> {

//    @Query(
//            value = "SELECT new sanity.nil.webchat.application.dto.message.ChatMessageDTO(m.id, m.sender.memberID, m.content, m.receivedAt) " +
//                    "FROM MessageModel m " +
//                    "WHERE (:chatID IS NULL OR :chatID = m.chat.chatID) " +
//                    "AND (:message IS NULL OR LOWER(CAST(m.content AS text)) LIKE LOWER(CAST(CONCAT('%', :message, '%') AS text))) " +
//                    "ORDER BY m.receivedAt ASC"
//    )
//    List<ChatMessageDTO> findByFilters(
//            @Param("chatID") UUID chatID,
//            @Param("message") String message,
//            Pageable pageable
//    );

    @Query(
            value = "SELECT new sanity.nil.webchat.infrastructure.db.postgres.projections.MessageDetailedView(" +
                    "m.id, m.sender.memberID, m.content, m.receivedAt, " +
                    "cast(STRING_AGG(CAST(f.id AS text), ',') AS text), cast(STRING_AGG(f.directory, ',') AS text)) " +
                    "FROM MessageModel m " +
                    "LEFT JOIN MessageFileModel mf ON m.id = mf.message.id " +
                    "LEFT JOIN FileMetadataModel f ON mf.file.id = f.id " +
                    "WHERE (:chatID IS NULL OR :chatID = m.chat.chatID) " +
                    "AND (:message IS NULL OR LOWER(CAST(m.content AS string)) LIKE LOWER(CONCAT('%', :message, '%'))) " +
                    "GROUP BY m.id, m.sender.memberID, m.content, m.receivedAt " +
                    "ORDER BY m.receivedAt ASC"
    )
    List<MessageDetailedView> findByFilters(
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
