package sanity.nil.webchat.infrastructure.storage.s3;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import sanity.nil.webchat.application.exceptions.StorageException;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileStorage {

    private final MinioConfig minio;

    public String createBucketIfNotExists(String... args) {
        String bucketName = "videos";
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

    public Mono<String> saveFile(FileData file, String name, String bucketName) {
        return Mono.fromCallable(() -> {
            try {
                if (!minio.getClient().bucketExists(BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build())) {
                    createBucketIfNotExists();
                }
                minio.getClient().putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(name)
                        .stream(file.content, file.size, -1)
                        .contentType(file.contentType)
                        .build());
                return name;
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
