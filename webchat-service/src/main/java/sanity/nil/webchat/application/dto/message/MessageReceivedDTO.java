package sanity.nil.webchat.application.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import sanity.nil.webchat.application.consts.MessageStatus;

import java.util.UUID;

public record MessageReceivedDTO(
        @JsonProperty("message_id") UUID messageID,
        @JsonProperty("status") MessageStatus status
) { }
