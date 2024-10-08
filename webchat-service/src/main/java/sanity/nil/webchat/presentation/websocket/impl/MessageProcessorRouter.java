package sanity.nil.webchat.presentation.websocket.impl;

import lombok.Setter;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.security.NoSuchAlgorithmException;

@Setter
public class MessageProcessorRouter<Session, Message> {

    private MessageProcessor<Session, Message> messageProcessor;
    private boolean active;

    public MessageProcessorRouter() {
        this.active = true;
    }

    public Publisher<Message> process(Session session, Message message) {
        if (active && messageProcessor != null) {
            return messageProcessor.process(session, message);
        }
        return Flux.error(new NoSuchAlgorithmException());
    }

    public void disable() {
        this.active = false;
    }
}
