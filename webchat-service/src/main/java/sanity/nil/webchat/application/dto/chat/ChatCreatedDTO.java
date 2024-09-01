package sanity.nil.webchat.application.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ChatCreatedDTO(
        @JsonProperty(value = "chat_id")
        UUID chatID
) { }
