package sanity.nil.webchat.application.dto.role;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AddMemberRoleDTO(
        @JsonProperty(value = "member_id")
        UUID memberID,
        @JsonProperty(value = "chat_id")
        UUID chatID,
        @JsonProperty(value = "role_id")
        UUID roleID
) { }
