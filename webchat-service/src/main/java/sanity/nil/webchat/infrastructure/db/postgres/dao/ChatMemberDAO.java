package sanity.nil.webchat.infrastructure.db.postgres.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatMemberID;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatMemberModel;

public interface ChatMemberDAO extends JpaRepository<ChatMemberModel, ChatMemberID> {
}
