package sanity.nil.webchat.infrastructure.db.postgres.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanity.nil.webchat.application.dto.ChatMemberDTO;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberModel;

import java.util.List;
import java.util.UUID;

public interface MemberDAO extends JpaRepository<MemberModel, UUID> {

    @Query(
            value = "SELECT m.* " +
                    "FROM members m " +
                    "LEFT JOIN chat_members cm " +
                    "ON m.id = cm.member_id " +
                    "WHERE cm.chat_id = :chatID ", nativeQuery = true
    )
    List<MemberModel> getAllByChatID(
            @Param("chatID") UUID chatID
    );

    @Query(
            value = "SELECT m.id " +
                    "FROM members m " +
                    "LEFT JOIN chat_members cm " +
                    "ON m.id = cm.member_id " +
                    "WHERE cm.chat_id = :chatID ", nativeQuery = true
    )
    List<UUID> getAllMemberIDsByChatID(
            @Param("chatID") UUID chatID
    );

    @Query(
            value = "SELECT new sanity.nil.webchat.application.dto.ChatMemberDTO(m.memberID, m.nickname, cm.memberRole.role) " +
                    "FROM MemberModel m " +
                    "LEFT JOIN ChatMemberModel cm " +
                    "ON m.memberID = cm.member.memberID " +
                    "WHERE cm.chat.chatID = :chatID "
    )
    List<ChatMemberDTO> getAllMembersByChatID(
            @Param("chatID") UUID chatID
    );
}
