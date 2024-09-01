package sanity.nil.webchat.application.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UploadedFilesDTO(
        @JsonProperty(value = "uploaded_files")
        List<FileUploadResultDTO> uploadedFiles
) { }
