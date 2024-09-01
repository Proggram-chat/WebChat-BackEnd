package sanity.nil.webchat.application.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.UUID;

public record FileURLDTO(
        @JsonProperty(value = "url")
        String url,
        @JsonProperty(value = "original_name")
        String originalName,
        @JsonProperty(value = "message_id")
        UUID messageID,
        @JsonProperty(value = "sender_id")
        UUID senderID,
        @JsonProperty(value = "sent_at")
        ZonedDateTime sentAt
) { }
