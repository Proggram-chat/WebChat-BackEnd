package sanity.nil.webchat.application.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import sanity.nil.webchat.application.consts.ChatType;

import java.util.UUID;

public record CreateChatDTO(
        @JsonProperty(value = "creator_id")
        UUID creatorID,
        @JsonProperty(value = "name")
        String name,
        @JsonProperty(value = "description")
        String description,
        @JsonProperty(value = "type")
        ChatType type
) { }
