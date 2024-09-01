package sanity.nil.webchat.application.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record OnModifyMessageDTO(
        @JsonProperty(value = "message_id")
        UUID messageID,
        @JsonProperty(value = "content")
        String content
) { }
