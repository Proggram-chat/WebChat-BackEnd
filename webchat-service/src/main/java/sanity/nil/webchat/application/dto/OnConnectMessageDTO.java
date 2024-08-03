package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OnConnectMessageDTO(
        @JsonProperty(value = "message_base")
        MessageBaseDTO messageBase,
        @JsonProperty(value = "messages")
        List<MessageDTO> messages
) { }
