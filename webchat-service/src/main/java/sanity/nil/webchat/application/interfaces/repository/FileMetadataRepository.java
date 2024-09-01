package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.application.consts.FileProcessStatus;
import sanity.nil.webchat.application.dto.file.FileMetadataDTO;
import sanity.nil.webchat.application.dto.file.FileSearchDTO;
import sanity.nil.webchat.infrastructure.db.postgres.model.FileMetadataModel;

import java.util.List;
import java.util.UUID;

public interface FileMetadataRepository {
    void save(FileMetadataModel fileMetadata);
    FileMetadataModel findById(UUID id);
    List<FileMetadataModel> findByIds(List<UUID> ids);
    void updateStatusByIds(List<UUID> ids, FileProcessStatus status);
    List<FileMetadataDTO> findBySearchDTO(FileSearchDTO searchDTO);
    List<FileMetadataModel> findByStatuses(List<FileProcessStatus> statuses);
    void deleteById(UUID id);
}
