package sanity.nil.webchat.infrastructure.db.postgres.projections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor
@Getter
@Setter
public class MessageDetailedView {

    private UUID messageID;
    private UUID senderID;
    private String content;
    private ZonedDateTime receivedAt;
    private List<FilePointer> filePointers;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class FilePointer {
        private String fileID;
        private String fileDir;
    }

    public MessageDetailedView(UUID messageID, UUID senderID, String content, ZonedDateTime receivedAt,
                               String fileIDs, String fileDirs) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.content = content;
        this.receivedAt = receivedAt;
        if (StringUtils.isNotBlank(fileIDs) && StringUtils.isNotBlank(fileDirs)) {
            List<String> fileIDList = Arrays.asList(fileIDs.split(","));
            List<String> fileDirList = Arrays.asList(fileDirs.split(","));
            this.filePointers = IntStream.range(0, fileIDList.size())
                    .mapToObj(i -> new FilePointer(fileIDList.get(i), fileDirList.get(i)))
                    .collect(Collectors.toList());
        }
    }
}
