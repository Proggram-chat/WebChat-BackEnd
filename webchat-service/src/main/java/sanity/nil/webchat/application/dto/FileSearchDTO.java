package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record FileSearchDTO(
        @JsonProperty(value = "chat_id")
        UUID chatID,
        @JsonProperty(value = "message_id")
        UUID messageID,
        @JsonProperty(value = "detailed")
        boolean detailed
) {}
