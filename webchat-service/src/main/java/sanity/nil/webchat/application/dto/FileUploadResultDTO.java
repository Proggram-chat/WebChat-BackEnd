package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import sanity.nil.webchat.application.consts.UploadResult;

import java.util.UUID;

public record FileUploadResultDTO(
        @JsonProperty(value = "file_id")
        UUID fileID,
        @JsonProperty(value = "upload_result")
        UploadResult uploadResult
) { }
