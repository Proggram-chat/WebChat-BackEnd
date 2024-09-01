package sanity.nil.webchat.application.dto.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import sanity.nil.webchat.application.consts.UploadResult;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FileUploadResultDTO(
        @JsonProperty(value = "file_id")
        UUID fileID,
        @JsonProperty(value = "upload_result")
        UploadResult uploadResult,
        @JsonProperty(value = "url", required = false)
        String url
) { }
