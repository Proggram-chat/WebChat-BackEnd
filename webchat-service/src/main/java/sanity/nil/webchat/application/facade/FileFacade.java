package sanity.nil.webchat.application.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import sanity.nil.webchat.application.consts.FileProcessStatus;
import sanity.nil.webchat.application.consts.UploadResult;
import sanity.nil.webchat.application.dto.file.*;
import sanity.nil.webchat.application.interfaces.helpers.FileSystemHelper;
import sanity.nil.webchat.application.interfaces.repository.FileMetadataRepository;
import sanity.nil.webchat.application.interfaces.storage.FileStorage;
import sanity.nil.webchat.infrastructure.db.postgres.model.FileMetadataModel;
import sanity.nil.webchat.infrastructure.storage.s3.FileData;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileFacade {

    private final FileStorage fileStorage;
    private final FileSystemHelper fileSystemHelper;
    private final FileMetadataRepository fileMetadataRepository;

    @Transactional
    public Mono<UploadedFilesDTO> uploadFiles(Flux<FilePart> files) {
        return files
                .publishOn(Schedulers.boundedElastic())
                .flatMap(filePart ->  {
                    log.info("Received a file: {}", filePart.filename());
                    UUID id = UUID.randomUUID();
                    try {

                        File fileOnFS = Files.createFile(Path.of(fileSystemHelper.getTempDirPath() + id + '.' +
                                StringUtils.substringAfterLast(filePart.filename(), "."))).toFile();

                        FileMetadataModel fileMetadataModel = new FileMetadataModel(id, filePart.filename(),
                                fileSystemHelper.evaluateFileType(fileOnFS), FileProcessStatus.AWAITING_UPLOAD);
                        fileMetadataModel.setDirectory(fileSystemHelper.getDestDirectory(fileMetadataModel.getType()));
                        fileMetadataRepository.save(fileMetadataModel);

                        return filePart.transferTo(fileOnFS)
                                .then(Mono.fromCallable(() -> {
                                    long fileSize = Files.size(fileOnFS.toPath());
                                    return new FileData(id.toString(), fileOnFS,
                                            fileSystemHelper.evaluateContentType(fileOnFS),
                                            fileMetadataModel.getDirectory(), fileSize);
                                }));
                    } catch (Exception e) {
                        log.error("Got an error saving a file in local system.: {}", e.getMessage());
                        return Mono.just(new FileData(id.toString()));
                    }
                })
                .flatMap(fileData -> {
                    if (!fileData.toUpload) {
                        return Mono.just(new FileUploadResultDTO(UUID.fromString(fileData.filename), UploadResult.FAIL, null));
                    }
                    log.info("Saving a file {} to minio", fileData.filename);
                    return fileStorage.saveFile(fileData)
                            .map(fileId -> {
                                String url = fileStorage.getFileURL(fileId, fileData.destinationDir);
                                return new FileUploadResultDTO(UUID.fromString(fileId), UploadResult.SUCCESS, url);
                            })
                            .onErrorResume(e -> {
                                log.debug("Failed to save a file {}: {}", fileData.filename, e.getMessage());
                                return Mono.just(new FileUploadResultDTO(UUID.fromString(fileData.filename), UploadResult.FAIL, null));
                            });
                })
                .collectList()
                .doOnSuccess(uploadedFileIds -> {
                    fileMetadataRepository.updateStatusByIds(
                            uploadedFileIds.stream()
                                    .filter(e -> e.uploadResult().equals(UploadResult.SUCCESS))
                                    .map(FileUploadResultDTO::fileID).toList(),
                            FileProcessStatus.SUCCESS_UPLOAD
                    );
                    fileMetadataRepository.updateStatusByIds(
                            uploadedFileIds.stream()
                                    .filter(e -> e.uploadResult().equals(UploadResult.FAIL))
                                    .map(FileUploadResultDTO::fileID).toList(),
                            FileProcessStatus.FAILED_UPLOAD
                    );
                })
                .map(UploadedFilesDTO::new);
    }

    public List<FileURLDTO> fileSearch(FileSearchDTO fileSearchDTO) {
        List<FileMetadataDTO> filesMetadata = fileMetadataRepository.findBySearchDTO(fileSearchDTO);
        return filesMetadata.stream().map(e -> {
            String url = fileStorage.getFileURL(e.id().toString(), e.directory());
            return new FileURLDTO(url, e.originalName(), e.messageID(), e.senderID(), e.sentAt());
        }).toList();
    }
}
