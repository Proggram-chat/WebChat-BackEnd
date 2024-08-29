package sanity.nil.webchat.infrastructure.db.postgres.model;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private MemberRoleModel memberRole;

    public ChatMemberModel(ChatModel chat, MemberModel member, MemberRoleModel memberRole) {
        this.id = new ChatMemberID(chat.getChatID(), member.getMemberID());
        this.chat = chat;
        this.member = member;
        this.memberRole = memberRole;
    }
}
