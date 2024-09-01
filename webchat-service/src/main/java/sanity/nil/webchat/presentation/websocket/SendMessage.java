package sanity.nil.webchat.presentation.websocket;

import sanity.nil.webchat.application.dto.message.MessageSentDTO;
import sanity.nil.webchat.presentation.websocket.impl.MessageToSend;

@FunctionalInterface
public interface SendMessage {
    MessageSentDTO send(MessageToSend message);
}
