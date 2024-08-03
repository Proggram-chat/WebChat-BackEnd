package sanity.nil.webchat.application.dto;

import lombok.AllArgsConstructor;
import sanity.nil.webchat.application.consts.PayloadType;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoPayloadBase;

import java.util.UUID;

public class MessageCreateDTO extends CentrifugoPayloadBase {

    public UUID id;
    public UUID chatID;
    public UUID senderID;
    public String content;

    public MessageCreateDTO(PayloadType payloadType,
                            UUID id, UUID chatID, UUID senderID, String content) {
        super(payloadType);
        this.id = id;
        this.chatID = chatID;
        this.senderID = senderID;
        this.content = content;
    }
}
