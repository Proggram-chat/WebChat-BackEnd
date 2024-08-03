package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import sanity.nil.webchat.application.consts.ChatType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public record MemberChatsDTO(
        @JsonProperty(value = "chat_id")
        UUID chatID,
        @JsonProperty(value = "chat_name")
        String chatName,
        @JsonProperty(value = "chat_type")
        ChatType chatType,
        @JsonProperty(value = "message_id")
        UUID messageID,
        @JsonProperty(value = "text_preview")
        String textPreview,
        @JsonProperty(value = "member_nickname")
        String memberNickname,
        @JsonProperty(value = "sent_at")
        ZonedDateTime sentAt
) { }
