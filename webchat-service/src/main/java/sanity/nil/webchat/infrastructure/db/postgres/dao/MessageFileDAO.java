package sanity.nil.webchat.infrastructure.db.postgres.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sanity.nil.webchat.infrastructure.db.postgres.model.MessageFileID;
import sanity.nil.webchat.infrastructure.db.postgres.model.MessageFileModel;

public interface MessageFileDAO extends JpaRepository<MessageFileModel, MessageFileID> {
}
