package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.application.dto.CreateChatDTO;
import sanity.nil.webchat.application.dto.MemberChatsDTO;

import java.util.List;
import java.util.UUID;

public interface ChatRepository {
    UUID createChat(CreateChatDTO dto);
    void deleteChat(UUID chatID);
    boolean existsChat(UUID chatID);
    List<MemberChatsDTO> getAllByMemberID(UUID memberID);
    void addMember(UUID memberID, UUID chatID);
    void deleteMember(UUID memberID, UUID chatID);
}
