package sanity.nil.webchat.infrastructure.db.postgres.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "message_files")
public class MessageFileModel {

    @EmbeddedId
    private MessageFileID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("messageID")
    private MessageModel message;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("fileID")
    private FileMetadataModel file;

    public MessageFileModel(UUID messageID, UUID fileId,
                            MessageModel message, FileMetadataModel file) {
        this.id = new MessageFileID(messageID, fileId);
        this.message = message;
        this.file = file;
    }
}
