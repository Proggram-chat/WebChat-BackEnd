package sanity.nil.webchat.application.dto;

import java.util.List;

public record UploadedFilesDTO(
        List<String> fileIds
) { }
