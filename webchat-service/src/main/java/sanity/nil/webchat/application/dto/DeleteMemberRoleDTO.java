package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record DeleteMemberRoleDTO(
        @JsonProperty(value = "chat_id")
        UUID chatID,
        @JsonProperty(value = "member_id")
        UUID memberID
) { }
