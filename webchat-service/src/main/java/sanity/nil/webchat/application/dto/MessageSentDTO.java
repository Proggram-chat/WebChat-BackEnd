package sanity.nil.webchat.application.dto;

import sanity.nil.webchat.application.consts.MessageStatus;

import java.util.UUID;

public record MessageSentDTO(
        UUID messageID,
        MessageStatus status
) { }