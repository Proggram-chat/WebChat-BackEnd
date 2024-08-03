package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public record ChatMessageDTO(
        @JsonProperty(value = "message_id")
        UUID messageID,
        @JsonProperty(value = "sender_id")
        UUID senderID,
        @JsonProperty(value = "content")
        String content,
        @JsonProperty(value = "sent_at")
        ZonedDateTime sentAt
) { }
