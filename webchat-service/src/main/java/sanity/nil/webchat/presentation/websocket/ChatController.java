package sanity.nil.webchat.presentation.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sanity.nil.webchat.application.consts.MessageType;
import sanity.nil.webchat.application.interfaces.storage.MemberSession;
import sanity.nil.webchat.application.interfaces.storage.MemberSessionStorage;
import sanity.nil.webchat.infrastructure.storage.keyval.WebsocketMemberSession;
import sanity.nil.webchat.presentation.websocket.impl.MessageProcessor;
import sanity.nil.webchat.presentation.websocket.impl.MessageProcessorRouter;

import java.io.IOException;
import java.util.UUID;

//@Component
@RequiredArgsConstructor
@Slf4j
public class ChatController implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MemberSessionStorage sessionStorage;
    private MessageProcessorRouter<WebSocketSession, WebSocketMessage> messageProcessorRouter = new MessageProcessorRouter<>();
    @Qualifier("onSendMessageProcessor")
    private final MessageProcessor<WebSocketSession, WebSocketMessage> onSendMessageProcessor;
    @Qualifier("onConnectUserMessageProcessor")
    private final MessageProcessor<WebSocketSession, WebSocketMessage> onConnectUserMessageProcessor;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        // Handshake
        String sessionID = session.getId();
        String user = session.getHandshakeInfo().getUri().getPath().split("/")[2];
        UUID userID = UUID.fromString(user);
        log.info("Connected client {}", sessionID);
        MemberSession memberSession = new WebsocketMemberSession(sessionID, userID, session, true);
        sessionStorage.saveIfNotExists(memberSession);

        Mono<Void> onClose = session.closeStatus()
                .flatMap(closeStatus -> {
                    return session.send(Mono.just(session.textMessage("Goodbye!")));
                })
                .then();

        // Reactive consumer on an action of sending to a socket
        Flux<WebSocketMessage> output = session.receive()
                .flatMap(socketMessage -> {
                    try {
                        JsonNode payload = objectMapper.readTree(socketMessage.getPayloadAsText());
                        MessageType messageType = MessageType.valueOf(payload.get("message_base").get("message_type").asText());
//                        socketMessage.retain();
                        switch (messageType) {
                            case SEND -> messageProcessorRouter.setMessageProcessor(onSendMessageProcessor);
                            case ON_CONNECT -> messageProcessorRouter.setMessageProcessor(onConnectUserMessageProcessor);
                        }
                        socketMessage.retain();
                    } catch (IOException e) {
                        return Flux.error(e);
                    }
                    return Flux.from(messageProcessorRouter.process(session,socketMessage))
                            .onErrorResume(e -> Mono.error(e))
                            .doFinally(signalType -> socketMessage.release());
                })
                .doOnError(ex -> {
                    sessionStorage.deleteBySessionID(sessionID);
                    log.info("Session {} deleted due to error {}", sessionID, ex.getMessage());
                });

        return session.send(output)
                .then(onClose)
                .doOnError(ex -> log.error(ex.getMessage()));
    }

}