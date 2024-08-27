package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.UUID;

public record FileMetadataDTO(
        UUID id,
        String originalName,
        String directory,
        UUID messageID,
        UUID senderID,
        ZonedDateTime sentAt
) { }
