package sanity.nil.webchat.infrastructure.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chat_members")
public class ChatMemberModel implements Serializable {

    @EmbeddedId
    private ChatMemberID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chatID")
    private ChatModel chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberID")
    private MemberModel member;

    @Column(name = "is_admin")
    public boolean isAdmin;

}
