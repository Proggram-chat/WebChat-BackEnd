package sanity.nil.webchat.infrastructure.db.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import sanity.nil.webchat.infrastructure.db.model.ChatMemberID;
import sanity.nil.webchat.infrastructure.db.model.ChatMemberModel;

public interface ChatMemberDAO extends JpaRepository<ChatMemberModel, ChatMemberID> {
}
