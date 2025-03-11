package faddy.image.dto;

import faddy.global.exception.ImageException;
import faddy.image.type.ImageCategory;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static faddy.global.exception.ExceptionCode.FAIL_IMAGE_NAME_HASH;
import static org.springframework.util.StringUtils.getFilenameExtension;

@Getter
public class ImageFile {

    private static final String EXTENSION_DELIMITER = ".";

    private final MultipartFile file;
    private final String hashedName;
    private String originalName;
    private Long size; // 파일 크기
    private String format; // 파일 형식
    private String url;
    private ImageCategory category;

    public ImageFile(final MultipartFile file , final String url , final ImageCategory category) {

        this.file = file;
        this.hashedName = hashName(file);
        this.originalName = file.getOriginalFilename();
        this.size = file.getSize();
        this.format = getFilenameExtension(file.getOriginalFilename());
        this.url = url;
        this.category = category;
    }


    private String hashName(final MultipartFile image) {

        final String filenameExtension = EXTENSION_DELIMITER + getFilenameExtension(originalName);
        final String nameAndDate = originalName + LocalDateTime.now();

        try {
            final MessageDigest hashAlgorithm = MessageDigest.getInstance("SHA3-256");

            final byte[] hashBytes = hashAlgorithm.digest(nameAndDate.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(hashBytes) + filenameExtension;

        } catch (final NoSuchAlgorithmException e) {
            throw new ImageException(FAIL_IMAGE_NAME_HASH);
        }
    }

    private String bytesToHex(final byte[] bytes) {
        return IntStream.range(0, bytes.length)
                .mapToObj(i -> String.format("%02x", bytes[i] & 0xff))
                .collect(Collectors.joining());
    }

    public String getContentType() {
        return this.file.getContentType();
    }

    public long getSize() {
        return this.file.getSize();
    }

    public InputStream getInputStream() throws IOException {
        return this.file.getInputStream();
    }

}
