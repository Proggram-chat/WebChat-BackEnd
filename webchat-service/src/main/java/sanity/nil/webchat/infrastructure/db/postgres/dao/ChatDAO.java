package sanity.nil.webchat.infrastructure.db.postgres.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanity.nil.webchat.application.dto.MemberChatsDTO;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatModel;

import java.util.List;
import java.util.UUID;

public interface ChatDAO extends JpaRepository<ChatModel, UUID> {

    @Query(
            value = "SELECT new sanity.nil.webchat.application.dto.MemberChatsDTO(c.chatID, c.name, c.type, msg.id, msg.content, m.nickname, msg.receivedAt) " +
                    "FROM MemberModel m " +
                    "LEFT JOIN ChatMemberModel cm ON cm.member.memberID = m.memberID " +
                    "LEFT JOIN ChatModel c ON cm.chat.chatID = c.chatID " +
                    "LEFT JOIN MessageModel msg ON msg.id = (" +
                    "    SELECT msg1.id " +
                    "    FROM MessageModel msg1 " +
                    "    WHERE msg1.chat.chatID = c.chatID " +
                    "    ORDER BY msg1.receivedAt DESC " +
                    "    LIMIT 1" +
                    ") " +
                    "WHERE m.memberID = :member_id " +
                    "ORDER BY msg.receivedAt DESC"
    )
    List<MemberChatsDTO> findByMemberID(@Param("member_id") UUID memberID);

}
