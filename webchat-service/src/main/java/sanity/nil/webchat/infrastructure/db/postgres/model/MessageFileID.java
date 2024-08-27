package sanity.nil.webchat.infrastructure.db.postgres.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class MessageFileID implements Serializable {

    @Column(name = "message_id")
    private UUID messageID;
    @Column(name = "file_id")
    private UUID fileID;

}
