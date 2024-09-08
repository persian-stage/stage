package amirhs.de.stage.service.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class S3StorageService implements StorageService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public S3StorageService(@Value("${cloud.aws.credentials.access-key}") String accessKey,
                            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
                            @Value("${cloud.aws.region.static}") String region,
                            @Value("${cloud.aws.s3.endpoint}") String endpoint) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    @Override
    public String uploadFile(String userId, MultipartFile file) throws IOException {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        String baseFileName = generateFileName(file.getOriginalFilename());
        String format = "jpg";

        String originalFileName = "user/" + userId + "/avatar/original/" + baseFileName + "." + format;
        byte[] originalBytes = convertImageFormat(file, format, -1, -1); // -1 means no resizing
        uploadToS3(originalFileName, originalBytes, format);

        String thumbnailFileName = "user/" + userId + "/avatar/thumbnail/" + baseFileName + "." + format;
        byte[] thumbnailBytes = convertImageFormat(file, format, 100, 100);
        uploadToS3(thumbnailFileName, thumbnailBytes, format);

        String mediumFileName = "user/" + userId + "/avatar/medium/" + baseFileName + "." + format;
        byte[] mediumBytes = convertImageFormat(file, format, 300, 300);
        uploadToS3(mediumFileName, mediumBytes, format);

        return s3Client.getUrl(bucketName, originalFileName).toString();
    }

    @Override
    public byte[] downloadFile(String path) throws IOException {
        return s3Client.getObject(bucketName, path).getObjectContent().readAllBytes();
    }

    @Override
    public void deleteFile(String path) throws IOException {
        s3Client.deleteObject(bucketName, path);
    }

    public byte[] convertImageFormat(MultipartFile file, String format, int width, int height) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Thumbnails.Builder<?> builder = Thumbnails.of(file.getInputStream())
                    .outputFormat(format);

            // If no resizing is needed, set a default scale factor of 1.0
            if (width == -1 && height == -1) {
                builder.scale(1.0);
            } else {
                builder.size(width, height);
            }

            builder.toOutputStream(baos);
            return baos.toByteArray();
        }
    }

    private void uploadToS3(String fileName, byte[] content, String format) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(content.length);
        metadata.setContentType("image/" + format);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
    }

    private String generateFileName(String originalFilename) {
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        String uniqueIdentifier = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        return timestamp + "_" + uniqueIdentifier;
    }
}