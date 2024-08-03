package sanity.nil.webchat.infrastructure.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "messages")
@Entity
public class MessageModel {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private ChatModel chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private MemberModel sender;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "received_at", columnDefinition = "timestamptz")
    private ZonedDateTime receivedAt;

//    @Column(name = "modifiedAt", columnDefinition = "timestamptz")
//    private ZonedDateTime modifiedAt;
}
