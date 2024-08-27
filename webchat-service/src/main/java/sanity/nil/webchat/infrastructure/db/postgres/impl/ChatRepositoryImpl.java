package sanity.nil.webchat.infrastructure.db.postgres.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.dto.CreateChatDTO;
import sanity.nil.webchat.application.dto.MemberChatsDTO;
import sanity.nil.webchat.application.interfaces.repository.ChatRepository;
import sanity.nil.webchat.infrastructure.db.postgres.dao.ChatDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.ChatMemberDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.MemberDAO;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatMemberID;
import sanity.nil.webchat.infrastructure.db.postgres.model.ChatModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberModel;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {

    private final ChatDAO chatDAO;
    private final MemberDAO memberDAO;
    private final ChatMemberDAO chatMemberDAO;

    @Override
    public UUID createChat(CreateChatDTO dto) {
        Optional<MemberModel> maybeCreator = memberDAO.findById(dto.creatorID());
        if (maybeCreator.isEmpty()) {
            throw new NoSuchElementException("No member with ID: " + dto.creatorID());
        }
        Set<MemberModel> members = new HashSet<>();
        members.add(maybeCreator.get());
        return chatDAO.save(new ChatModel(UUID.randomUUID(), dto.name(), dto.description(), members, dto.type())).getChatID();
    }

    @Override
    public void deleteChat(UUID chatID) {
        chatDAO.deleteById(chatID);
    }

    @Override
    public boolean existsChat(UUID chatID) {
        return chatDAO.findById(chatID).isPresent();
    }

    @Override
    public List<MemberChatsDTO> getAllByMemberID(UUID memberID) {
        return chatDAO.findByMemberID(memberID);
    }

    @Transactional
    @Override
    public void addMember(UUID memberID, UUID chatID) {
        Optional<ChatModel> maybeChat = chatDAO.findById(chatID);
        if (maybeChat.isEmpty()) {
            throw new NoSuchElementException("No member with ID: " + chatID);
        }
        Optional<MemberModel> maybeMember = memberDAO.findById(memberID);
        if (maybeMember.isEmpty()) {
            throw new NoSuchElementException("No member with ID: " + memberID);
        }
        maybeChat.get().addMember(maybeMember.get());
        chatDAO.save(maybeChat.get()).getChatID();
    }

    @Transactional
    @Override
    public void deleteMember(UUID memberID, UUID chatID) {
        chatMemberDAO.deleteById(new ChatMemberID(chatID, memberID));
    }
}
