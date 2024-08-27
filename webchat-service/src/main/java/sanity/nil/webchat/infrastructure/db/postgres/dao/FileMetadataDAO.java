package sanity.nil.webchat.infrastructure.db.postgres.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanity.nil.webchat.application.consts.FileUploadStatus;
import sanity.nil.webchat.application.dto.FileMetadataDTO;
import sanity.nil.webchat.application.dto.FileURLDTO;
import sanity.nil.webchat.infrastructure.db.postgres.model.FileMetadataModel;

import java.util.List;
import java.util.UUID;

public interface FileMetadataDAO extends JpaRepository<FileMetadataModel, UUID> {

    @Modifying
    @Query(
            value = "UPDATE file_metadata " +
                    "SET upload_status = :status " +
                    "WHERE id IN (:ids) ",
            nativeQuery = true
    )
    void updateStatusByIds(
            @Param("ids") List<UUID> ids,
            @Param("status") String status
    );

    @Query(
            value = "SELECT new sanity.nil.webchat.application.dto.FileMetadataDTO(fm.id, fm.filename, fm.directory, m.id, m.sender.memberID, fm.updatedAt) " +
                    "FROM FileMetadataModel fm " +
                    "INNER JOIN MessageFileModel mfm ON fm.id = mfm.file.id " +
                    "INNER JOIN MessageModel m ON m.id = mfm.message.id " +
                    "INNER JOIN ChatModel c ON c.chatID = m.chat.chatID " +
                    "WHERE (:message_id IS NULL OR :message_id = m.id) " +
                    "AND (:chat_id IS NULL OR :chat_id = c.chatID)" +
                    "ORDER BY fm.updatedAt"
    )
    List<FileMetadataDTO> findAllBySearchFilters(
            @Param("chat_id") UUID chatID,
            @Param("message_id") UUID messageID);
}
