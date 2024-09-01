package sanity.nil.webchat.application.dto.message;

import sanity.nil.webchat.application.consts.PayloadType;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoPayloadBase;

import java.util.UUID;

public class MessageCreateDTO extends CentrifugoPayloadBase {

    public UUID id;
    public UUID chatID;
    public UUID senderID;
    public String content;

    public MessageCreateDTO(UUID id, UUID chatID, UUID senderID, String content) {
        super(PayloadType.MESSAGE_CREATED);
        this.id = id;
        this.chatID = chatID;
        this.senderID = senderID;
        this.content = content;
    }
}
