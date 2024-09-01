package sanity.nil.webchat.infrastructure.db.postgres.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.interfaces.repository.MemberRoleRepository;
import sanity.nil.webchat.infrastructure.db.postgres.dao.ChatDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.FunctionDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.MemberRoleDAO;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.FunctionModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberRoleModel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MemberRoleRepositoryImpl implements MemberRoleRepository {

    private final MemberRoleDAO memberRoleDAO;
    private final ChatDAO chatDAO;
    private final FunctionDAO functionDAO;

    @Override
    public UUID addRoleType(UUID chatID, String roleType, String functions) {
        Optional<ChatModel> maybeChat = chatDAO.findById(chatID);
        if (maybeChat.isEmpty()) {
            throw new NoSuchElementException("No chat with ID: " + chatID);
        }
        MemberRoleModel newRoleModel = new MemberRoleModel(maybeChat.get(), roleType.toUpperCase(), functions);
        return memberRoleDAO.save(newRoleModel).getId();
    }

    @Override
    public Optional<MemberRoleModel> getByChatAndRoleType(UUID chatID, String roleType) {
        return memberRoleDAO.findByChatIdAndType(chatID, roleType);
    }

    @Override
    public List<FunctionModel> getAllFunctions() {
        List<FunctionModel> functions = functionDAO.findAll();
        functions.removeFirst();
        return functions;
    }
}
