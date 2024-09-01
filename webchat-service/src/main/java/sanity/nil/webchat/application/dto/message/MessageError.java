package sanity.nil.webchat.application.dto.message;

import java.util.UUID;

public record MessageError(
        UUID messageID,
        UUID chatID,
        String error
) { }
