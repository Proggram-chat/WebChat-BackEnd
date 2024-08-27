package sanity.nil.webchat.infrastructure.db.postgres.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.interfaces.repository.MemberRepository;
import sanity.nil.webchat.infrastructure.db.postgres.dao.ChatDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.ChatMemberDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.MemberDAO;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatMemberID;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatMemberModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberModel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberDAO memberDAO;
    private final ChatDAO chatDAO;
    private final ChatMemberDAO chatMemberDAO;

    @Override
    public UUID save(MemberModel member) {
        return memberDAO.save(member).getMemberID();
    }

    @Override
    public List<MemberModel> getAllByChatID(UUID chat) {
        return memberDAO.getAllByChatID(chat);
    }

    @Override
    public Optional<MemberModel> getByMemberID(UUID memberID) {
        return memberDAO.findById(memberID);
    }

    @Override
    public List<UUID> getMemberIdsByChatID(UUID chatID) {
        return memberDAO.getAllMemberIDsByChatID(chatID);
    }

    @Override
    public void removeMemberFromChat(UUID memberID, UUID chatID) {
        chatMemberDAO.deleteById(new ChatMemberID(chatID, memberID));
    }

    @Override
    public void addMemberToChat(UUID memberID, UUID chatID) {
        ChatModel chat = chatDAO.findById(chatID).orElseThrow(
                () -> new NoSuchElementException("No chat to add a member exists")
        );
        MemberModel memberToAdd = memberDAO.findById(memberID).orElseThrow(
                () -> new NoSuchElementException("No such member exists to add")
        );
        chatMemberDAO.save(new ChatMemberModel(chat, memberToAdd, false));
    }
}
