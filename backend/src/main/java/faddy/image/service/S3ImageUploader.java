package faddy.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.ImageException;
import faddy.image.type.ImageCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class S3ImageUploader implements ImageUploader{

    private final AmazonS3 amazonS3;
    private final String bucket;
    private final String directory;
    private final String region;
    private final AmazonS3 s3Client;

    private static final String EXTENSION_DELIMITER = ".";
    private static final String DIRECTORY_DELIMITER = "/";

    public S3ImageUploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket,
                           @Value("${cloud.aws.s3.dir}") String directory,
                           @Value("${cloud.aws.region.static}") String region,
                           AmazonS3 s3Client) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
        this.directory = directory;
        this.region = region;
        this.s3Client = s3Client;
    }

    @Override
    public String upload(MultipartFile multipartFile, ImageCategory category) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        // 카테고리를 포함한 파일 경로 설정
        String fileName = this.directory + DIRECTORY_DELIMITER + category.name().toLowerCase() + DIRECTORY_DELIMITER + uniqueFileName;

        File uploadFile = convert(multipartFile);

        String objUrl = putS3(uploadFile, fileName);

        log.info("objUrl : " + objUrl);
        log.info("filename : " + fileName);
        removeNewFile(uploadFile);

        return objUrl;
    }

    private File convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                throw new ImageException(ExceptionCode.IMAGE_CONVERT_ERROR);
            }
            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
    }

    private String putS3(File uploadFile, String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHttpExpiresDate(new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000));

        PutObjectRequest request = new PutObjectRequest(bucket, fileName, uploadFile);
        request.setMetadata(metadata);
        request.setCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(request);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName);
        long expirationTime = System.currentTimeMillis() + (6 * 24 * 60 * 60 * 1000L);
        Date expiration = new Date(expirationTime);
        generatePresignedUrlRequest.setExpiration(expiration);
        URL objUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return objUrl.toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("Deleting file from S3: " + decodedFileName);
            amazonS3.deleteObject(bucket, decodedFileName);
        } catch (UnsupportedEncodingException e) {
            log.error("Error while decoding the file name: {}", e.getMessage());
            throw new ImageException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while decoding the file name: " + e.getMessage());
        }
    }

    @Override
    public void deleteFiles(List<String> fileNames) {
        for (String fileName : fileNames) {
            try {
                String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
                log.info("Deleting file from S3: {}", decodedFileName);
                amazonS3.deleteObject(bucket, decodedFileName);
            } catch (UnsupportedEncodingException e) {
                log.error("Error while decoding the file name: {}", e.getMessage());
                throw new ImageException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while decoding the file name: " + e.getMessage());
            }
        }
    }

    @Override
    public String updateFile(MultipartFile newFile, String oldFileName ,ImageCategory category) throws IOException {
        log.info("S3 oldFileName: " + oldFileName);
        deleteFile(oldFileName);
        return upload(newFile , category);
    }

    private String createObjectUrl(String fileName) {
        return new StringBuilder()
                .append("https://")
                .append(bucket)
                .append(".s3.")
                .append(region)
                .append(".amazonaws.com/")
                .append(directory)
                .append("/")
                .append(fileName)
                .toString();
    }

    public URL getObjectUrl(String bucketName, String objectKey) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey);
        return s3Client.generatePresignedUrl(request);
    }

    @Override
    public String extractObjectKeyFromObjUrl(String objUrl) throws MalformedURLException {
        URL url = new URL(objUrl);
        String path = url.getPath().substring(1);
        return path;
    }
}
