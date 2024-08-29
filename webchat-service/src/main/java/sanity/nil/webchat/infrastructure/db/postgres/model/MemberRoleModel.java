package sanity.nil.webchat.infrastructure.db.postgres.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member_roles")
public class MemberRoleModel {

    @Id
    private UUID id;

    @ManyToOne
    private ChatModel chat;

    @Column(name = "role", nullable = false)
    private String role;

    public MemberRoleModel(ChatModel chat, String role) {
        this.id = UUID.randomUUID();
        this.chat = chat;
        this.role = role;
    }
}
