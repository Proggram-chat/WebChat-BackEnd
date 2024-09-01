package sanity.nil.webchat.application.interfaces.helpers;

import org.springframework.http.MediaType;
import sanity.nil.webchat.application.consts.FileType;

import java.io.File;

public interface FileSystemHelper {
    String getDestDirectory(FileType fileType);
    FileType evaluateFileType(MediaType mediaType);
    String evaluateContentType(File file);
    FileType evaluateFileType(File file);
    String getTempDirPath();
}
