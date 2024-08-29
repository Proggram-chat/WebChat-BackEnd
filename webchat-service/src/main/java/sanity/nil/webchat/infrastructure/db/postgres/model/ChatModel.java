package sanity.nil.webchat.infrastructure.db.postgres.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sanity.nil.webchat.application.consts.ChatType;

import java.util.*;

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

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "chat_members",
//            joinColumns = @JoinColumn(name = "chat_id"),
//            inverseJoinColumns = @JoinColumn(name = "member_id")
//    )
//    private Set<MemberModel> members = new HashSet<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatMemberModel> chatMembers = new HashSet<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageModel> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRoleModel> memberRoles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ChatType type;

    public void addMember(ChatMemberModel member) {
        if (member != null) {
            chatMembers.add(member);
        }
    }

    public void addMemberRoles(MemberRoleModel... roleModels) {
        if (roleModels != null) {
            memberRoles.addAll(Arrays.stream(roleModels).toList());
        }
    }

    public ChatModel(UUID chatID, String name, String description,
                     ChatType type) {
        this.chatID = chatID;
        this.name = name;
        this.description = description;
        this.type = type;
    }
}
