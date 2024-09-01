package sanity.nil.webchat.infrastructure.storage.fs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import sanity.nil.webchat.application.consts.FileType;
import sanity.nil.webchat.application.interfaces.helpers.FileSystemHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FileSystemHelperImpl implements FileSystemHelper {

    private Map<FileType, List<String>> supportedTypes = new HashMap<>(4);

    {
        supportedTypes.put(FileType.AUDIO, List.of("audio/mp3", "audio/ogg", "audio/wav"));
        supportedTypes.put(FileType.VIDEO, List.of("video/mp4", "video/mpeg", "video/mkv", "video/webm"));
        supportedTypes.put(FileType.IMAGE, List.of("image/png", "image/jpeg", "image/jpg", "image/gif"));
        supportedTypes.put(FileType.OTHER, List.of("application/octet-stream"));
    }

    public String getTempDirPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public FileType evaluateFileType(MediaType mediaType) {
        FileType evaluatedType = FileType.OTHER;
        for (FileType type : supportedTypes.keySet()) {
            if (supportedTypes.get(type).contains(mediaType.toString())) {
                evaluatedType = type;
                break;
            }
        }
        return evaluatedType;
    }

    public String getDestDirectory(FileType fileType) {
        return fileType.name().toLowerCase();
    }

}
