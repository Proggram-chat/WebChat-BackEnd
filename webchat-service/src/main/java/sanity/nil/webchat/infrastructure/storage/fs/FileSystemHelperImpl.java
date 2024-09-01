package sanity.nil.webchat.infrastructure.storage.fs;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import sanity.nil.webchat.application.consts.FileType;
import sanity.nil.webchat.application.interfaces.helpers.FileSystemHelper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FileSystemHelperImpl implements FileSystemHelper {

    private Map<FileType, List<String>> supportedTypes = new HashMap<>(4);
    private Tika tika = new Tika();

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

    public FileType evaluateFileType(File file) {
        try {
            FileType evaluatedType = FileType.OTHER;
            String fileType = tika.detect(file);
            if (StringUtils.isBlank(fileType))
                throw new IllegalArgumentException();

            for (FileType type : supportedTypes.keySet()) {
                if (supportedTypes.get(type).contains(fileType)) {
                    evaluatedType = type;
                    break;
                }
            }
            return evaluatedType;
        } catch (Exception e) {
            log.error("Error evaluating a fileType: {}", e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String evaluateContentType(File file) {
        try {
            return tika.detect(file);
        } catch (Exception e) {
            log.error("Error evaluating a contentType: {}", e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    public String getDestDirectory(FileType fileType) {
        return fileType.name().toLowerCase();
    }

}
