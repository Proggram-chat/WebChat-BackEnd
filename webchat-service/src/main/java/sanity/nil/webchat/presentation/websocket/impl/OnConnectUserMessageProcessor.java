package sanity.nil.webchat.presentation.websocket.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import sanity.nil.webchat.application.dto.message.OnConnectMessageDTO;

import java.io.IOException;
import java.util.UUID;

//@Component
@Slf4j
@RequiredArgsConstructor
public class OnConnectUserMessageProcessor implements MessageProcessor<WebSocketSession, WebSocketMessage> {

    private final ObjectMapper objectMapper;

    @Override
    public Publisher<WebSocketMessage> process(WebSocketSession session, WebSocketMessage socketMessage) {
        try {
            OnConnectMessageDTO onConnectMessage;
            onConnectMessage = objectMapper.readValue(socketMessage.getPayloadAsText(), OnConnectMessageDTO.class);
            log.info("Last received messages: {}", onConnectMessage.messages().size());
//            socketMessage.retain();
            UUID messageID = UUID.randomUUID();
            return Flux.just();
        } catch (IOException e) {
            return Flux.error(e);
        }
    }
}
