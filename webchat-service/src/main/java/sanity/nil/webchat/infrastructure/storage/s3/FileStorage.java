package sanity.nil.webchat.infrastructure.storage.s3;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import sanity.nil.webchat.application.exceptions.StorageException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileStorage {

    private final MinioConfig minio;

    public String createBucketIfNotExists(String bucketName) {
        try {
            if (!minio.getClient().bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build())) {

                minio.getClient().makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Created minio bucket with name {}", bucketName);
            }
        } catch (Exception e) {
            throw new StorageException(e.getMessage());
        }
        return bucketName;
    }

    public Mono<String> saveFile(FileData fileData) {
        return Mono.fromCallable(() -> {
            try (FileInputStream fileInputStream = new FileInputStream(fileData.file)) {

                // Check if bucket exists, if not create it
                if (!minio.getClient().bucketExists(BucketExistsArgs.builder()
                        .bucket(fileData.destinationDir)
                        .build())) {
                    createBucketIfNotExists(fileData.destinationDir);
                }

                // Upload file
                minio.getClient().putObject(PutObjectArgs.builder()
                        .bucket(fileData.destinationDir)
                        .object(fileData.filename)
                        .stream(fileInputStream, fileData.size, -1)
                        .contentType(fileData.contentType)
                        .build());

                return fileData.filename;
            } catch (ErrorResponseException e) {
                String detailedMessage = e.getMessage();
                try (Response response = e.response()) {
                    if (response != null) {
                        detailedMessage = response.message();
                    }
                }
                throw new StorageException(detailedMessage);
            } catch (Exception e) {
                throw new StorageException(e.getMessage());
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public String getFileURL(String name, String bucketName) {
        String url = null;
        try {
            url = minio.getClient().getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(name)
                            .expiry(2, TimeUnit.HOURS)
                            .versionId(null)
                            .build());
            log.info("Url to minio object: {}", url);
        } catch (Exception e) {
            throw new StorageException(e.getMessage());
        }
        return url;
    }
}
