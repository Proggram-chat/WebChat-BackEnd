package sanity.nil.webchat.application.interfaces.storage;

import reactor.core.publisher.Mono;
import sanity.nil.webchat.infrastructure.storage.s3.FileData;

public interface FileStorage {
    Mono<String> saveFile(FileData fileData);
    String getFileURL(String name, String directory);

}
