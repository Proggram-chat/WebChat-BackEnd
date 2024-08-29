package sanity.nil.webchat.infrastructure.db.postgres.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.dto.ChatMemberDTO;
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
    public List<ChatMemberDTO> getMembersByChatID(UUID chatID) {
        return memberDAO.getAllMembersByChatID(chatID);
    }
}
