package sanity.nil.webchat.application.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sanity.nil.webchat.application.dto.chat.*;
import sanity.nil.webchat.application.dto.member.OnJoinMemberDTO;
import sanity.nil.webchat.application.dto.member.OnLeaveMemberDTO;
import sanity.nil.webchat.application.dto.role.AddChatRoleDTO;
import sanity.nil.webchat.application.dto.role.AddMemberRoleDTO;
import sanity.nil.webchat.application.dto.role.DeleteMemberRoleDTO;
import sanity.nil.webchat.application.interfaces.repository.ChatRepository;
import sanity.nil.webchat.application.interfaces.repository.MemberRepository;
import sanity.nil.webchat.application.interfaces.repository.MemberRoleRepository;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoBroadcastPayload;
import sanity.nil.webchat.infrastructure.channels.impl.CentrifugoHelper;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberModel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatFacade {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final CentrifugoHelper centrifugoHelper;

    @Transactional
    public ChatCreatedDTO createChat(CreateChatDTO createChatDTO) {
        UUID chatID = chatRepository.createChat(createChatDTO);
        return new ChatCreatedDTO(chatID);
    }

    public void deleteChat(UUID chatID) {
        chatRepository.deleteChat(chatID);
    }

    @Transactional
    public void joinChat(JoinChatDTO joinChatDTO) {
        MemberModel joinedMember = memberRepository.getByMemberID(joinChatDTO.memberID())
                .orElseThrow(
                        () -> new NoSuchElementException("No member found with id " + joinChatDTO.memberID())
                );
        List<UUID> members = memberRepository.getMemberIdsByChatID(joinChatDTO.chatID());
        List<String> channels = members.stream()
                .map(e -> "personal:" + e.toString()).toList();
        chatRepository.addMemberToChat(joinChatDTO.memberID(), joinChatDTO.chatID());
        centrifugoHelper.broadcast(new CentrifugoBroadcastPayload(channels,
                new OnJoinMemberDTO(joinChatDTO.memberID(), joinedMember.getNickname()),
                "member_joined_" + joinChatDTO.memberID())
        ).subscribe(
                res -> {
                    log.info("A user {} joined chat {}, successfully handed to centrifugo {}",
                            joinChatDTO.memberID(), joinChatDTO.chatID(), res);
                }, err -> {
                    log.error("Error sending a join user event to centrifugo {}", err.getMessage());
                }
        );

    }

    @Transactional
    public void leaveChat(LeaveChatDTO leaveChatDTO) {
        MemberModel leftMember = memberRepository.getByMemberID(leaveChatDTO.memberID())
                .orElseThrow(
                        () -> new NoSuchElementException("No member found with id " + leaveChatDTO.memberID())
                );
        List<UUID> members = memberRepository.getMemberIdsByChatID(leaveChatDTO.chatID());
        List<String> channels = members.stream()
                .map(e -> "personal:" + e.toString()).toList();
        chatRepository.removeMemberFromChat(leaveChatDTO.memberID(), leaveChatDTO.chatID());
        centrifugoHelper.broadcast(new CentrifugoBroadcastPayload(channels,
                new OnLeaveMemberDTO(leaveChatDTO.memberID(), leftMember.getNickname()),
                "member_left_" + leaveChatDTO.memberID())
        ).subscribe(
                res -> {
                    log.info("A user {} left chat {}, successfully handed to centrifugo {}",
                            leaveChatDTO.memberID(), leaveChatDTO.chatID(), res);
                }, err -> {
                    log.error("Error sending a leave user event to centrifugo {}", err.getMessage());
                }
        );
    }

    public UUID addChatRole(AddChatRoleDTO dto) {
        String functions = dto.functions().stream()
                .distinct()
                .sorted((e,e1) -> e > e1 ? -1 : 1)
                .map(String::valueOf)
                .reduce((e,e1) -> String.join(",", e1, e))
                .orElseThrow(() -> new NoSuchElementException("No functions were specified for a role"));
        log.info(functions);
        return memberRoleRepository.addRoleType(dto.chatID(), dto.roleType(), functions);
    }

    public void addMemberRole(AddMemberRoleDTO dto) {
        chatRepository.addMemberChatRole(dto.memberID(), dto.chatID(), dto.roleID());
    }

    public void deleteMemberRole(DeleteMemberRoleDTO dto) {
        chatRepository.deleteMemberChatRole(dto.memberID(), dto.chatID());
    }

    public List<ChatMemberDTO> getChatMembers(UUID chatID) {
        return memberRepository.getMembersByChatID(chatID);
    }

}
