package sanity.nil.webchat.application.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sanity.nil.webchat.application.dto.chat.MemberChatsDTO;
import sanity.nil.webchat.application.interfaces.repository.ChatRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberFacade {

    private final ChatRepository chatRepository;

    public List<MemberChatsDTO> getAllMemberChats(UUID memberID) {
        return chatRepository.getAllByMemberID(memberID);
    }
}
