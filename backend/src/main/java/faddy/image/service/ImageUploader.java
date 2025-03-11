package faddy.image.service;

import faddy.image.domain.Image;
import faddy.image.type.ImageCategory;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface ImageUploader {

    String upload(MultipartFile multipartFile , ImageCategory category) throws IOException;

    void deleteFile(String fileName);

    void deleteFiles(List<String> fileNames);

    String updateFile(MultipartFile newFile, String oldFileName ,ImageCategory category) throws IOException;

    String extractObjectKeyFromObjUrl(String objUrl) throws MalformedURLException;
}
