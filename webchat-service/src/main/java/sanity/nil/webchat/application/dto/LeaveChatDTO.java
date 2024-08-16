package sanity.nil.webchat.application.dto;

import java.util.UUID;

public record LeaveChatDTO(
        UUID memberID,
        UUID chatID
) { }
