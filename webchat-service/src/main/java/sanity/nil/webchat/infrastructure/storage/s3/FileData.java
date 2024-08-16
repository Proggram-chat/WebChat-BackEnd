package sanity.nil.webchat.infrastructure.storage.s3;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import java.io.InputStream;
import java.util.List;

@AllArgsConstructor
public class FileData {

    public String filename;
    public InputStream content;
    public String contentType;
    public long size;

    public FileData(FilePart filePart) {
        this.filename = filePart.filename();
        List<String> contentTypes = filePart.headers().get("Content-Type");
        if (CollectionUtils.isEmpty(contentTypes)) {
            throw new IllegalArgumentException("Content-Type header is missing");
        }
        this.contentType = contentTypes.getFirst();

        Flux<DataBuffer> contentFlux = filePart.content();
        DataBuffer dataBuffer = DataBufferUtils.join(contentFlux).block();
        if (dataBuffer != null) {
            this.size = dataBuffer.readableByteCount();
            this.content = dataBuffer.asInputStream();
            DataBufferUtils.release(dataBuffer);
        } else {
            this.size = 0;
            this.content = InputStream.nullInputStream();
        }
    }

}
