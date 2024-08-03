package sanity.nil.webchat.infrastructure.db.model;

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
public class ChatMemberID implements Serializable {

    @Column(name = "chat_id")
    private UUID chatID;
    @Column(name = "member_id")
    private UUID memberID;

}
