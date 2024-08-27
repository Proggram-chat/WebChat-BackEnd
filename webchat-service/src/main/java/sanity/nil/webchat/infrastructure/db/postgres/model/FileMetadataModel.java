package sanity.nil.webchat.infrastructure.db.postgres.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sanity.nil.webchat.application.consts.FileType;
import sanity.nil.webchat.application.consts.FileUploadStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "file_metadata")
public class FileMetadataModel {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "filename")
    private String filename;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FileType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "upload_status")
    private FileUploadStatus uploadStatus;

    @Column(name = "directory")
    private String directory;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    private ZonedDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    public FileMetadataModel(UUID id, String filename, FileType type, FileUploadStatus uploadStatus) {
        this.id = id;
        this.filename = filename;
        this.type = type;
        this.uploadStatus = uploadStatus;
    }
}
