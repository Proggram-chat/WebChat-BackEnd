package sanity.nil.webchat.infrastructure.storage.s3;

import lombok.AllArgsConstructor;

import java.io.File;

@AllArgsConstructor
public class FileData {

    public String filename;
    public File file;
    public String contentType;
    public String destinationDir;
    public boolean toUpload;
    public long size;

    public FileData(String filename, File file, String contentType, String destinationDir, long size) {
        this.filename = filename;
        this.file = file;
        this.contentType = contentType;
        this.destinationDir = destinationDir;
        this.toUpload = true;
        this.size = size;
    }

    public FileData(String filename) {
        this.filename = filename;
        this.toUpload = false;
    }

}
