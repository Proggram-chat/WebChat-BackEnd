package sanity.nil.webchat.infrastructure.db.postgres.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.consts.MemberRoleType;
import sanity.nil.webchat.application.dto.CreateChatDTO;
import sanity.nil.webchat.application.dto.MemberChatsDTO;
import sanity.nil.webchat.application.interfaces.repository.ChatRepository;
import sanity.nil.webchat.infrastructure.db.postgres.dao.ChatDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.ChatMemberDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.MemberDAO;
import sanity.nil.webchat.infrastructure.db.postgres.dao.MemberRoleDAO;
import sanity.nil.webchat.infrastructure.db.postgres.model.*;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {

    private final ChatDAO chatDAO;
    private final MemberDAO memberDAO;
    private final ChatMemberDAO chatMemberDAO;
    private final MemberRoleDAO memberRoleDAO;

    @Override
    public UUID createChat(CreateChatDTO dto) {
        Optional<MemberModel> maybeCreator = memberDAO.findById(dto.creatorID());
        if (maybeCreator.isEmpty()) {
            throw new NoSuchElementException("No member with ID: " + dto.creatorID());
        }
        ChatModel chat = new ChatModel(UUID.randomUUID(), dto.name(),
                dto.description(), dto.type());
        chatDAO.save(chat);

        MemberRoleModel memberRoleModel = new MemberRoleModel(chat, MemberRoleType.MEMBER.name());
        MemberRoleModel adminRoleModel = new MemberRoleModel(chat, MemberRoleType.ADMIN.name());
        memberRoleDAO.save(memberRoleModel);
        memberRoleDAO.save(adminRoleModel);

        chat.addMemberRoles(memberRoleModel, adminRoleModel);
        chat.addMember(new ChatMemberModel(chat, maybeCreator.get(), adminRoleModel));
        return chatDAO.save(chat).getChatID();
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
        Optional<MemberRoleModel> maybeRole = memberRoleDAO.findByChatIdAndType(chatID, MemberRoleType.MEMBER.name());
        if (maybeRole.isEmpty()) {
            throw new NoSuchElementException("No member role exists to add");
        }
        chatMemberDAO.save(new ChatMemberModel(chat, memberToAdd, maybeRole.get()));
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

    @Override
    public void addMemberChatRole(UUID memberID, UUID chatID, UUID roleID) {
        MemberRoleModel memberRole = memberRoleDAO.findById(roleID)
                .orElseThrow(() -> new NoSuchElementException("No such memberRole exists to add"));
        ChatMemberModel chatMember = chatMemberDAO.findById(new ChatMemberID(chatID, memberID))
                .orElseThrow(() -> new NoSuchElementException("No such chatMember exists"));
        chatMember.setMemberRole(memberRole);
        chatMemberDAO.save(chatMember);
    }

    @Override
    public void deleteMemberChatRole(UUID memberID, UUID chatID) {
        ChatMemberModel chatMember = chatMemberDAO.findById(new ChatMemberID(chatID, memberID))
                .orElseThrow(() -> new NoSuchElementException("No such chatMember exists"));
        MemberRoleModel memberRole =
                new MemberRoleModel(chatMember.getChat(), MemberRoleType.MEMBER.name());
        memberRoleDAO.save(memberRole);
        chatMember.setMemberRole(memberRole);
        chatMemberDAO.save(chatMember);
    }
}
