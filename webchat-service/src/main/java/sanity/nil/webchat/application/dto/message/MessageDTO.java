package sanity.nil.webchat.application.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record MessageDTO(
        @JsonProperty(value = "message_id")
        UUID messageID,
        @JsonProperty(value = "chat_id")
        UUID chatID,
        @JsonProperty(value = "sender_id")
        UUID senderID
) { }
