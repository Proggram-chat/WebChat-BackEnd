package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.infrastructure.db.postgres.model.MessageFileModel;

import java.util.List;

public interface MessageFileRepository {
    void saveAll(List<MessageFileModel> messageFiles);
}
