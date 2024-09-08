package amirhs.de.stage.service.aws;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3StorageServiceV2 implements StorageService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public S3StorageServiceV2(@Value("${cloud.aws.credentials.access-key}") String accessKey,
                              @Value("${cloud.aws.credentials.secret-key}") String secretKey,
                              @Value("${cloud.aws.region.static}") String region) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
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

        return s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(originalFileName).build()).toString();
    }

    @Override
    public byte[] downloadFile(String path) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build();
        return s3Client.getObject(getObjectRequest).readAllBytes();
    }

    @Override
    public void deleteFile(String path) throws IOException {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    public boolean deleteDirectoryRecursively(String directoryPrefix) {
        // Set up pagination for listing objects
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(directoryPrefix)
                .build();

        ListObjectsV2Response listObjectsResponse;
        boolean allDeletedSuccessfully = true;

        // Continue to list objects and delete them until there are no more
        do {
            listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

            // Get the objects in the response
            if (!listObjectsResponse.contents().isEmpty()) {
                // Prepare the list of objects to delete
                List<ObjectIdentifier> toDelete = new ArrayList<>();
                listObjectsResponse.contents().forEach(s3Object ->
                        toDelete.add(ObjectIdentifier.builder().key(s3Object.key()).build())
                );

                // Create the delete request
                DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                        .bucket(bucketName)
                        .delete(Delete.builder().objects(toDelete).build())
                        .build();

                // Execute the delete request
                DeleteObjectsResponse deleteObjectsResponse = s3Client.deleteObjects(deleteRequest);

                // Check if any objects failed to delete
                if (!deleteObjectsResponse.hasErrors()) {
                    System.out.println("Deleted " + deleteObjectsResponse.deleted().size() + " objects successfully.");
                } else {
                    deleteObjectsResponse.errors().forEach(error -> {
                        System.err.println("Failed to delete: " + error.key() + " due to: " + error.message());
                    });
                    allDeletedSuccessfully = false;
                }
            }

            // Continue to the next page of results if available
            listObjectsRequest = listObjectsRequest.toBuilder()
                    .continuationToken(listObjectsResponse.nextContinuationToken())
                    .build();

        } while (listObjectsResponse.isTruncated());

        return allDeletedSuccessfully;
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
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("image/" + format)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
    }

    private String generateFileName(String originalFilename) {
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        String uniqueIdentifier = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        return timestamp + "_" + uniqueIdentifier;
    }
}
