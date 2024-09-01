package sanity.nil.webchat.application.dto.message;

import org.checkerframework.checker.units.qual.A;
import sanity.nil.webchat.application.consts.PayloadType;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoPayloadBase;

import java.util.List;
import java.util.UUID;

public class MessageCreateDTO extends CentrifugoPayloadBase {

    public UUID id;
    public UUID chatID;
    public UUID senderID;
    public String content;
    public List<AttachmentDTO> attachments;

    public MessageCreateDTO(UUID id, UUID chatID, UUID senderID, String content,
                            List<AttachmentDTO> attachments) {
        super(PayloadType.MESSAGE_CREATED);
        this.id = id;
        this.chatID = chatID;
        this.senderID = senderID;
        this.content = content;
        this.attachments = attachments;
    }
}
