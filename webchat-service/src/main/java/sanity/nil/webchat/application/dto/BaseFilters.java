package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BaseFilters {
    @JsonProperty(value = "limit")
    public Integer limit;
    @JsonProperty(value = "offset")
    public Integer offset;
}
