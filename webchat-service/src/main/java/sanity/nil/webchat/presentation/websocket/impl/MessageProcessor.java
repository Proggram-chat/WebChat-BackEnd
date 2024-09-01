package sanity.nil.webchat.presentation.websocket.impl;

import org.reactivestreams.Publisher;

@FunctionalInterface
public interface MessageProcessor<Session, Message> {
    Publisher<Message> process(Session session, Message message);
}
