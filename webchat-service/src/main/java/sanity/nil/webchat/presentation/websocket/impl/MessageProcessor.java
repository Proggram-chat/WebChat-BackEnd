package sanity.nil.webchat.presentation.websocket.impl;

import org.reactivestreams.Publisher;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

@FunctionalInterface
public interface MessageProcessor<Session, Message> {
    Publisher<Message> process(Session session, Message message);
}
