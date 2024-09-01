package sanity.nil.webchat.application.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.UUID;

public record MessageCreatedDTO(
        @JsonProperty(value = "message_id")
        UUID messageID,
        @JsonProperty(value = "sent_at")
        ZonedDateTime sentAt
) { }
