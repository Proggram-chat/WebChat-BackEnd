package sanity.nil.webchat.presentation.websocket.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sanity.nil.webchat.application.consts.MessageStatus;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MessageToSend {

    private UUID id;
    private UUID senderID;
    private UUID chatID;
    private String content;
    private MessageStatus status;
}
