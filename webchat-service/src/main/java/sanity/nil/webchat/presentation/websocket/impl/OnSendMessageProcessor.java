package sanity.nil.webchat.presentation.websocket.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sanity.nil.webchat.application.dto.OnSendMessageDTO;
import sanity.nil.webchat.application.interfaces.repository.ChatRepository;
import sanity.nil.webchat.application.interfaces.repository.MessageRepository;
import sanity.nil.webchat.presentation.websocket.MessageSender;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OnSendMessageProcessor implements MessageProcessor<WebSocketSession, WebSocketMessage> {

    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Override
    public Publisher<WebSocketMessage> process(WebSocketSession session, WebSocketMessage socketMessage) {
        try {
            OnSendMessageDTO onSendMessage;
            onSendMessage = objectMapper.readValue(socketMessage.getPayloadAsText(), OnSendMessageDTO.class);
//            socketMessage.retain();
            UUID messageID = UUID.randomUUID();
            ZonedDateTime sentAt = ZonedDateTime.now();
            log.info(sentAt.toString());
            if (!chatRepository.existsChat(onSendMessage.chatID())) {
                throw new NoSuchElementException("Chat with ID " + onSendMessage.chatID() + " not found");
            }
            return Mono.fromFuture(messageSender.sendMessageAsync(messageID, onSendMessage.senderID(), onSendMessage.chatID(), onSendMessage.content()))
                    .doOnNext(sent -> {
                        messageRepository.save(sent.messageID(), onSendMessage.chatID(), onSendMessage.senderID(),
                                onSendMessage.content(), sentAt);
                        log.info("Saved message with id: {}", sent.messageID());
                    })
                    .flatMap(sent -> {
                        WebSocketMessage webSocketMessage;
                        try {
                            webSocketMessage = session.textMessage(objectMapper.writeValueAsString(sent));
                            return Mono.just(webSocketMessage);
                        } catch (Exception e) {
                            log.error("Exception inside flatmap");
                            return Mono.error(e);
//                            String error = "Failed to process message";
//                            MessageError messageError = new MessageError(messageID, message.chatID(), error);
//                            webSocketMessage = session.textMessage(objectMapper.writeValueAsString(messageError));
                        }
                    });
        } catch (Exception e) {
            log.error("General exception");
            return Flux.error(e);
        }
    }
}
