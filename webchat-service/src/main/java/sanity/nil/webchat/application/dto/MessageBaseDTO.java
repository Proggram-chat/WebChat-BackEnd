package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import sanity.nil.webchat.application.consts.MessageType;

public record MessageBaseDTO(
        @JsonProperty(value = "messageType")
        MessageType messageType
) { }
