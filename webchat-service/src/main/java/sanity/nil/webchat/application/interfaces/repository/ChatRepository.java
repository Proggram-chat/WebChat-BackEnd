package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.application.dto.CreateChatDTO;
import sanity.nil.webchat.application.dto.MemberChatsDTO;
import sanity.nil.webchat.infrastructure.db.model.ChatModel;

import java.util.List;
import java.util.UUID;

public interface ChatRepository {
    UUID createChat(CreateChatDTO dto);
    boolean existsChat(UUID chatID);
    List<MemberChatsDTO> getAllByMemberID(UUID memberID);
    UUID addUser(UUID memberID, UUID chatID);
}
