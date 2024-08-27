package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.application.consts.FileUploadStatus;
import sanity.nil.webchat.application.dto.FileMetadataDTO;
import sanity.nil.webchat.application.dto.FileSearchDTO;
import sanity.nil.webchat.application.dto.FileURLDTO;
import sanity.nil.webchat.infrastructure.db.postgres.model.FileMetadataModel;

import java.util.List;
import java.util.UUID;

public interface FileMetadataRepository {
    void save(FileMetadataModel fileMetadata);
    FileMetadataModel findById(UUID id);
    void updateStatusByIds(List<UUID> ids, FileUploadStatus status);
    List<FileMetadataDTO> findBySearchDTO(FileSearchDTO searchDTO);
    void deleteById(UUID id);
}
