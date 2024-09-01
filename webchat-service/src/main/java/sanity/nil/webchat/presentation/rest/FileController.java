package sanity.nil.webchat.presentation.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sanity.nil.webchat.application.dto.file.FileSearchDTO;
import sanity.nil.webchat.application.dto.file.FileURLDTO;
import sanity.nil.webchat.application.dto.file.UploadedFilesDTO;
import sanity.nil.webchat.application.facade.FileFacade;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/file")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class FileController {

    private final FileFacade fileFacade;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<UploadedFilesDTO>> uploadFiles(
            @RequestPart("files") Flux<FilePart> files
    ) {
        return fileFacade.uploadFiles(files)
                .map(response -> ResponseEntity
                        .status(CREATED)
                        .body(response)
                );
    }

    @Operation(description = "Searches files by filters")
    @PostMapping(value = "/search")
    public ResponseEntity<List<FileURLDTO>> getFileURLs(
            @RequestBody FileSearchDTO fileSearchDTO
    ) {
        return ResponseEntity
                .status(OK)
                .body(fileFacade.fileSearch(fileSearchDTO));
    }
}
