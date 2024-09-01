package sanity.nil.webchat.presentation.websocket.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sanity.nil.webchat.application.consts.MessageStatus;
import sanity.nil.webchat.application.dto.message.MessageSentDTO;
import sanity.nil.webchat.application.interfaces.repository.MemberRepository;
import sanity.nil.webchat.application.interfaces.storage.MemberSession;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberModel;
import sanity.nil.webchat.infrastructure.storage.keyval.KeyValMemberSessionStorage;
import sanity.nil.webchat.infrastructure.storage.keyval.WebsocketMemberSession;
import sanity.nil.webchat.presentation.websocket.SendMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
//@Component
@RequiredArgsConstructor
public class DefaultSender implements SendMessage {

    private final MemberRepository memberRepository;
    private final KeyValMemberSessionStorage sessionStorage;
    private final ObjectMapper objectMapper;

    @Override
    public MessageSentDTO send(MessageToSend message) {
        List<MemberModel> members = new ArrayList<>(memberRepository.getAllByChatID(message.getChatID()));

        Optional<MemberModel> author = members.stream().filter(e -> e.getMemberID().equals(message.getSenderID())).findFirst();
        if (author.isPresent()) {
            members.remove(author.get());
        }
        List<MemberSession> memberSessions = sessionStorage.getSessionsByMemberIDs(members.stream()
                .map(MemberModel::getMemberID)
                .collect(Collectors.toList()));
        log.info("Starting to send messages to websocket connections");
        Flux.fromIterable(memberSessions)
                .map(m -> (WebsocketMemberSession) m)
                .map(WebsocketMemberSession::getWebSocketSession)
                .flatMap(session -> {
                    try {
                        log.info("Sent message {} to session {}", message.getId(), session.getId());
                        return session.send(Mono.just(session.textMessage(objectMapper.writeValueAsString(message))));
                    } catch (IOException e) {
                        return Mono.error(e);
                    }
                })
                .subscribe(
                        null,
                        e -> log.error("Error sending message: " + e.getMessage())
                );
        log.info("Finished sending messages to websocket connections");
        return new MessageSentDTO(message.getId(), MessageStatus.DELIVERED);
    }
}
