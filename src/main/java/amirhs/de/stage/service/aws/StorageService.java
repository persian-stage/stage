package amirhs.de.stage.service.aws;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String uploadFile(String path, MultipartFile file) throws IOException;
    byte[] downloadFile(String path) throws IOException;
    void deleteFile(String path) throws IOException;
}
