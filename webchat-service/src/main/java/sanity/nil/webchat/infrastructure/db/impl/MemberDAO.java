package sanity.nil.webchat.infrastructure.db.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanity.nil.webchat.infrastructure.db.model.MemberModel;

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
    List<MemberModel> getAllByChatID(@Param("chatID") UUID chatID);
}
