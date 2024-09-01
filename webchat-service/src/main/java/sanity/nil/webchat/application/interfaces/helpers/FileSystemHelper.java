package sanity.nil.webchat.application.interfaces.helpers;

import org.springframework.http.MediaType;
import sanity.nil.webchat.application.consts.FileType;

public interface FileSystemHelper {
    String getDestDirectory(FileType fileType);
    FileType evaluateFileType(MediaType mediaType);
    String getTempDirPath();
}
