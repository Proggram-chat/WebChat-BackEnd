package sanity.nil.webchat.infrastructure.db.postgres.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberRoleModel;

import java.util.Optional;
import java.util.UUID;

public interface MemberRoleDAO extends JpaRepository<MemberRoleModel, UUID> {

    @Query(
            value = "SELECT * FROM member_roles " +
                    "WHERE chat_id = :chatID " +
                    "AND role = :type", nativeQuery = true
    )
    Optional<MemberRoleModel> findByChatIdAndType(
            @Param("chatID") UUID chatID,
            @Param("type") String type
    );
}
