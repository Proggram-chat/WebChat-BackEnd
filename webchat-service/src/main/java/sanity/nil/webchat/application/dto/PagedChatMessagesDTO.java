package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PagedChatMessagesDTO(
        @JsonProperty(value = "messages")
        List<ChatMessageDTO> messages,
        @JsonProperty(value = "total_pages")
        int totalPages,
        @JsonProperty(value = "current_page")
        int currentPage
) { }
