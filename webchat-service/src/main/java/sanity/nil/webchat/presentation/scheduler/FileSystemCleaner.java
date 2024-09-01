package sanity.nil.webchat.presentation.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sanity.nil.webchat.application.interfaces.helpers.FileSystemHelper;
import sanity.nil.webchat.application.interfaces.repository.FileMetadataRepository;
import sanity.nil.webchat.infrastructure.db.postgres.model.FileMetadataModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static sanity.nil.webchat.application.consts.FileProcessStatus.*;

@Component
@EnableScheduling
@EnableAsync
@Slf4j
@RequiredArgsConstructor
public class FileSystemCleaner {

    private final FileSystemHelper fileSystemHelper;
    private final FileMetadataRepository fileMetadataRepository;

    @Async
    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 40 * 1000)
    public void task() {
        List<FileMetadataModel> uploadedFiles = fileMetadataRepository.findByStatuses(List.of(SUCCESS_UPLOAD));
        for (FileMetadataModel fileMetadataModel : uploadedFiles) {
            String fullPath = fileSystemHelper.getTempDirPath();
            try {
                fullPath += fileMetadataModel.getId() + "." + StringUtils.substringAfterLast(fileMetadataModel.getFilename(), '.');
                log.info(fullPath);
                boolean deleted = Files.deleteIfExists(Path.of(fullPath));
                if (deleted) {
                    fileMetadataModel.setProcessStatus(PROCESSED);
                    fileMetadataRepository.save(fileMetadataModel);
                } else {
                    log.error("Error cleaning a temp file: {}", fullPath);
                    fileMetadataModel.setProcessStatus(ERROR);
                    fileMetadataRepository.save(fileMetadataModel);
                }
            } catch (IOException e) {
                log.error("Error cleaning a temp file: {}, {}", fullPath, e.getMessage());
                fileMetadataModel.setProcessStatus(ERROR);
                fileMetadataRepository.save(fileMetadataModel);
            }
        }
    }
}
