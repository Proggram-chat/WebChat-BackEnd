package sanity.nil.webchat.presentation.websocket;

import sanity.nil.webchat.application.dto.MessageSentDTO;
import sanity.nil.webchat.presentation.websocket.impl.MessageToSend;

import java.util.UUID;

@FunctionalInterface
public interface SendMessage {
    MessageSentDTO send(MessageToSend message);
}
