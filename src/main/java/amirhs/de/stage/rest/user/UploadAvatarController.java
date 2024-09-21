package amirhs.de.stage.rest.user;

import amirhs.de.stage.apps.profiles.entity.Image;
import amirhs.de.stage.apps.profiles.entity.Profile;
import amirhs.de.stage.common.ResponseWrapper;
import amirhs.de.stage.service.UserService;
import amirhs.de.stage.service.aws.S3StorageServiceV2;
import amirhs.de.stage.service.aws.StorageService;
import amirhs.de.stage.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/file")
public class UploadAvatarController {

    private static final Logger logger = LogManager.getLogger(UploadAvatarController.class);

    private final StorageService storageService;
    private final UserService userService;
    private final S3StorageServiceV2 s3StorageServiceV2;

    public UploadAvatarController(@Qualifier("s3StorageService") StorageService storageService, UserService userService, S3StorageServiceV2 s3StorageServiceV2) {
        this.storageService = storageService;
        this.userService = userService;
        this.s3StorageServiceV2 = s3StorageServiceV2;
    }

    @PostMapping("/avatar")
    public ResponseEntity<ResponseWrapper> uploadFile(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        Optional<User> currentUser = userService.getUserWithEmail(userDetails.getUsername());

        logger.info("File Upload started: {}", userDetails.getUsername());

        if (currentUser.isEmpty()) {
            logger.warn("User not found: {}", userDetails.getUsername());
            ResponseWrapper response = new ResponseWrapper()
                    .add("redirectUrl", "/login")
                    .add("status", HttpStatus.FORBIDDEN.value() + "");
            return ResponseEntity.ok(response);
        }

        String avatarName = currentUser.get().getAvatar();
        if (avatarName != null && !avatarName.isEmpty()) {
            s3StorageServiceV2.deleteDirectoryRecursively("user/" + currentUser.get().getId() + "/avatar/");
        }

        String url = storageService.uploadFile(String.valueOf(currentUser.get().getId()), file);

        String filename = extractFilename(url);

        User user = currentUser.get();
        user.setAvatar(filename);

        userService.updateUser(user);

        ResponseWrapper response = new ResponseWrapper()
                .add("redirectUrl", "/profiles")
                .add("avatarUrl", user.getAvatar())
                .add("status", HttpStatus.OK.value() + "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete-avatar")
    public ResponseEntity<ResponseWrapper> deleteAvatar(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<User> currentUser = userService.getUserWithEmail(userDetails.getUsername());

        logger.info("File Upload started: {}", userDetails.getUsername());

        if (currentUser.isEmpty()) {
            logger.warn("User not found: {}", userDetails.getUsername());
            ResponseWrapper response = new ResponseWrapper()
                    .add("redirectUrl", "/login")
                    .add("status", HttpStatus.FORBIDDEN.value() + "");
            return ResponseEntity.ok(response);
        }

        String avatarName = currentUser.get().getAvatar();
        if (avatarName != null && !avatarName.isEmpty()) {
            s3StorageServiceV2.deleteDirectoryRecursively("user/" + currentUser.get().getId() + "/avatar/");
        }

        User user = currentUser.get();
        user.setAvatar("");

        userService.updateUser(user);

        ResponseWrapper response = new ResponseWrapper()
                .add("redirectUrl", "/profiles")
                .add("status", HttpStatus.OK.value() + "");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/image")
    public ResponseEntity<ResponseWrapper> imageFile(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException {
        Optional<User> currentUser = userService.getUserWithEmail(userDetails.getUsername());

        logger.info("Image File Upload started: {}", userDetails.getUsername());

        if (currentUser.isEmpty()) {
            logger.warn("User not found: {}", userDetails.getUsername());
            ResponseWrapper response = new ResponseWrapper()
                    .add("redirectUrl", "/login")
                    .add("status", HttpStatus.FORBIDDEN.value() + "");
            return ResponseEntity.ok(response);
        }

        String url = "";
        try {
            url = s3StorageServiceV2.uploadImage(String.valueOf(currentUser.get().getId()), file);
        } catch (Exception e) {
            logger.error("Error uploading image for user: {}", userDetails.getUsername(), e);
            ResponseWrapper response = new ResponseWrapper()
                    .add("error", "Image upload failed")
                    .add("status", HttpStatus.INTERNAL_SERVER_ERROR.value() + "");
            return ResponseEntity.ok(response);
        }

        String filename = extractFilename(url);

        User user = currentUser.get();

        Profile userProfile = user.getProfile();
        List<Image> currentImages = userProfile.getImages();
        Image userImage = new Image(filename, userProfile);

        currentImages.add(userImage);

        user.getProfile().setImages(currentImages);

        userService.updateUser(user);

        ResponseWrapper response = new ResponseWrapper()
                .add("images", currentImages)
                .add("status", HttpStatus.OK.value() + "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{fileName}")
    public byte[] downloadFile(@PathVariable String fileName) throws IOException {
        return storageService.downloadFile("user-avatars/" + fileName);
    }

    @DeleteMapping("/delete/{fileName}")
    public void deleteFile(@PathVariable String fileName) throws IOException {
        storageService.deleteFile("user-avatars/" + fileName);
    }

    public static String extractFilename(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}