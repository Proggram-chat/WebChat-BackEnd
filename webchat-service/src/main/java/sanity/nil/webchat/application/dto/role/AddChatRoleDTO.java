package sanity.nil.webchat.application.dto.role;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record AddChatRoleDTO(
        @JsonProperty(value = "chat_id")
        UUID chatID,
        @JsonProperty(value = "role_type")
        String roleType,
        @JsonProperty(value = "functions")
        List<Integer> functions
) { }
