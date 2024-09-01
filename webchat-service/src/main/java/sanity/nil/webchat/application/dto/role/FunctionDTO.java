package sanity.nil.webchat.application.dto.role;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FunctionDTO(
        @JsonProperty(value = "code")
        Integer code,
        @JsonProperty(value = "action")
        String action
) { }
