package sanity.nil.webchat.infrastructure.db.postgres.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sanity.nil.webchat.application.consts.FileProcessStatus;
import sanity.nil.webchat.application.dto.file.FileMetadataDTO;
import sanity.nil.webchat.application.dto.file.FileSearchDTO;
import sanity.nil.webchat.application.interfaces.repository.FileMetadataRepository;
import sanity.nil.webchat.infrastructure.db.postgres.dao.FileMetadataDAO;
import sanity.nil.webchat.infrastructure.db.postgres.model.FileMetadataModel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileMetadataRepositoryImpl implements FileMetadataRepository {

    private final FileMetadataDAO fileMetadataDAO;

    @Override
    public void save(FileMetadataModel fileMetadata) {
        fileMetadataDAO.save(fileMetadata);
    }

    @Override
    public FileMetadataModel findById(UUID id) {
        return fileMetadataDAO.findById(id).orElseThrow(
                () -> new NoSuchElementException("No chat to add a member exists")
        );
    }

    @Override
    public List<FileMetadataModel> findByIds(List<UUID> ids) {
        return fileMetadataDAO.findAllByIdIn(ids);
    }

    @Override
    public void deleteById(UUID id) {
        fileMetadataDAO.deleteById(id);
    }

    @Transactional
    @Override
    public void updateStatusByIds(List<UUID> ids, FileProcessStatus status) {
        fileMetadataDAO.updateStatusByIds(ids, status.name());
    }

    @Override
    public List<FileMetadataDTO> findBySearchDTO(FileSearchDTO searchDTO) {
        return fileMetadataDAO.findAllBySearchFilters(searchDTO.chatID(), searchDTO.messageID());
    }

    @Override
    public List<FileMetadataModel> findByStatuses(List<FileProcessStatus> statuses) {
        return fileMetadataDAO.findAllByProcessStatusIn(statuses);
    }
}
