package sanity.nil.webchat.application.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class FileUploadException extends RuntimeException {

    private String failedFileID;

    public FileUploadException(Throwable cause, String failedFileID) {
        super("File upload failed." , cause);
        this.failedFileID = failedFileID;
    }

    public FileUploadException(String failedFileID) {
        super("File upload failed.");
        this.failedFileID = failedFileID;
    }
}
