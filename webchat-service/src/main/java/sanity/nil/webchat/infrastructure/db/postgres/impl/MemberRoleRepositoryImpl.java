package sanity.nil.webchat.infrastructure.db.postgres.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.consts.MemberRoleType;
import sanity.nil.webchat.application.interfaces.repository.MemberRoleRepository;
import sanity.nil.webchat.infrastructure.db.postgres.dao.ChatDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.MemberRoleDAO;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberRoleModel;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MemberRoleRepositoryImpl implements MemberRoleRepository {

    private final MemberRoleDAO memberRoleDAO;
    private final ChatDAO chatDAO;

    @Override
    public String addRoleType(UUID chatID, String roleType) {
        Optional<ChatModel> maybeChat = chatDAO.findById(chatID);
        if (maybeChat.isEmpty()) {
            throw new NoSuchElementException("No chat with ID: " + chatID);
        }
        MemberRoleModel newRoleModel = new MemberRoleModel(maybeChat.get(), roleType.toUpperCase());
        return memberRoleDAO.save(newRoleModel).getRole();
    }

    @Override
    public Optional<MemberRoleModel> getByChatAndRoleType(UUID chatID, String roleType) {
        return memberRoleDAO.findByChatIdAndType(chatID, roleType);
    }
}
