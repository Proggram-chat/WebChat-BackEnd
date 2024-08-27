package sanity.nil.webchat.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record UploadedFilesDTO(
        @JsonProperty(value = "uploaded_files")
        List<FileUploadResultDTO> uploadedFiles
) { }
