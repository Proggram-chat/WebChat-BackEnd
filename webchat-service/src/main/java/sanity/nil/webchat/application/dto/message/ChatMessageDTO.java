package sanity.nil.webchat.application.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ChatMessageDTO(
        @JsonProperty(value = "message_id")
        UUID messageID,
        @JsonProperty(value = "sender_id")
        UUID senderID,
        @JsonProperty(value = "content")
        String content,
        @JsonProperty(value = "sent_at")
        ZonedDateTime sentAt,
        @JsonProperty(value = "attachments")
        List<String> attachments
) { }
