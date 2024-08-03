package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class MessageFiltersDTO extends BaseFilters {

    @JsonProperty(value = "message")
    public String message;
    @JsonProperty(value = "chat_id")
    public UUID chatID;
    @JsonProperty(value = "saved")
    public boolean saved;

    public MessageFiltersDTO(Integer limit, Integer offset, String message, UUID chatID, boolean saved) {
        super(limit, offset);
        this.message = message;
        this.chatID = chatID;
        this.saved = saved;
    }
}
