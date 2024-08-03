package sanity.nil.webchat.infrastructure.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sanity.nil.webchat.application.consts.ChatType;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chats")
@Entity
public class ChatModel {

    @Id
    @Column(name = "id")
    private UUID chatID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chat_members",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<MemberModel> members;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    public void addMember(MemberModel member) {
        if (member != null) {
            members.add(member);
        }
    }
}
