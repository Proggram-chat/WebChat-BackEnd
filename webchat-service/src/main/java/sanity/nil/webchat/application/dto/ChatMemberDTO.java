package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ChatMemberDTO(
        @JsonProperty(value = "member_id")
        UUID memberID,
        @JsonProperty(value = "member_name")
        String memberName
) { }
