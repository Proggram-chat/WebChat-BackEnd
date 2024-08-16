package sanity.nil.webchat.infrastructure.storage.s3;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import sanity.nil.webchat.application.exceptions.StorageException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
public class MinioConfig {

    private MinioClient minioClient;

    @Value("${application.minio.host}")
    private String host;

    @Value("${application.minio.port}")
    private Integer port;

    @Value("${application.minio.access-key}")
    private String accessKey;

    @Value("${application.minio.secret-key}")
    private String secretKey;

    @Value("${application.minio.secure}")
    private Boolean secure;

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(host, port, secure)
                .credentials(accessKey, secretKey)
                .build();
        try {
            checkMinioHealth();
            log.info("Minio is accessible and alive at url {}:{}", host, port);
        } catch (StorageException e) {
            log.error("Failed to connect to Minio: {}", e.getMessage());
            throw new StorageException("Failed to connect to Minio: " + e.getMessage());
        }
    }

    private void checkMinioHealth() {
        try {
            minioClient.bucketExists(BucketExistsArgs.builder().bucket("test").build());
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new StorageException(e);
        }
    }

    public MinioClient getClient() {
        return minioClient;
    }
}
