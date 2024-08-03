package sanity.nil.webchat.infrastructure.db.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.dto.CreateChatDTO;
import sanity.nil.webchat.application.dto.MemberChatsDTO;
import sanity.nil.webchat.application.interfaces.repository.ChatRepository;
import sanity.nil.webchat.infrastructure.db.model.ChatModel;
import sanity.nil.webchat.infrastructure.db.model.MemberModel;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {

    private final ChatDAO chatDAO;
    private final MemberDAO memberDAO;

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
    public boolean existsChat(UUID chatID) {
        return chatDAO.findById(chatID).isPresent();
    }

    @Override
    public List<MemberChatsDTO> getAllByMemberID(UUID memberID) {
        return chatDAO.findByMemberID(memberID);
    }

    @Transactional
    @Override
    public UUID addUser(UUID memberID, UUID chatID) {
        Optional<ChatModel> maybeChat = chatDAO.findById(chatID);
        if (maybeChat.isEmpty()) {
            throw new NoSuchElementException("No member with ID: " + chatID);
        }
        Optional<MemberModel> maybeMember = memberDAO.findById(memberID);
        if (maybeMember.isEmpty()) {
            throw new NoSuchElementException("No member with ID: " + memberID);
        }
        maybeChat.get().addMember(maybeMember.get());
        return chatDAO.save(maybeChat.get()).getChatID();
    }
}
