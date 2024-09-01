package sanity.nil.webchat.application.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AttachmentDTO(
        @JsonProperty(value = "id")
        UUID id,
        @JsonProperty(value = "url")
        String url
) { }
