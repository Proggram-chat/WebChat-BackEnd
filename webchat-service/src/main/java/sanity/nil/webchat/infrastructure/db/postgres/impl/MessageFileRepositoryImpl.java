package sanity.nil.webchat.infrastructure.db.postgres.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.interfaces.repository.MessageFileRepository;
import sanity.nil.webchat.infrastructure.db.postgres.dao.MessageFileDAO;
import sanity.nil.webchat.infrastructure.db.postgres.model.MessageFileModel;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageFileRepositoryImpl implements MessageFileRepository {

    private final MessageFileDAO messageFileDAO;

    @Override
    public void saveAll(List<MessageFileModel> messageFiles) {
        messageFileDAO.saveAll(messageFiles);
    }
}
