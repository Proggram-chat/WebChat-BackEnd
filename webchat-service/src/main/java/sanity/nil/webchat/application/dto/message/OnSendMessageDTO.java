package sanity.nil.webchat.application.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record OnSendMessageDTO(
        @JsonProperty(value = "chat_id")
        UUID chatID,
        @JsonProperty(value = "sender_id")
        UUID senderID,
        @JsonProperty(value = "content")
        String content,
        @JsonProperty(value = "attachments")
        List<String> attachments
) { }
