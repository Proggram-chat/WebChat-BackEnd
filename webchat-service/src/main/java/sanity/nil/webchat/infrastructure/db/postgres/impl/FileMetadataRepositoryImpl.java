package sanity.nil.webchat.infrastructure.db.postgres.impl;

import lombok.RequiredArgsConstructor;
import okio.FileMetadata;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sanity.nil.webchat.application.consts.FileUploadStatus;
import sanity.nil.webchat.application.dto.FileMetadataDTO;
import sanity.nil.webchat.application.dto.FileSearchDTO;
import sanity.nil.webchat.application.dto.FileURLDTO;
import sanity.nil.webchat.application.interfaces.repository.FileMetadataRepository;
import sanity.nil.webchat.infrastructure.db.postgres.dao.FileMetadataDAO;
import sanity.nil.webchat.infrastructure.db.postgres.model.FileMetadataModel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    public void deleteById(UUID id) {
        fileMetadataDAO.deleteById(id);
    }

    @Transactional
    @Override
    public void updateStatusByIds(List<UUID> ids, FileUploadStatus status) {
        fileMetadataDAO.updateStatusByIds(ids, status.name());
    }

    @Override
    public List<FileMetadataDTO> findBySearchDTO(FileSearchDTO searchDTO) {
        return fileMetadataDAO.findAllBySearchFilters(searchDTO.chatID(), searchDTO.messageID());
    }
}
