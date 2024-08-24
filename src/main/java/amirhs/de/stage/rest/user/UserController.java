package amirhs.de.stage.rest.user;

import amirhs.de.stage.auth.AuthenticationResponse;
import amirhs.de.stage.dto.UserDTO;
import amirhs.de.stage.service.UserService;
import amirhs.de.stage.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<AuthenticationResponse> getUser(@AuthenticationPrincipal UserDetails UserDetails) {
        Optional<User> currentUser = userService.getUserWithEmail(UserDetails.getUsername());

        if (currentUser.isEmpty()) {
            AuthenticationResponse response = AuthenticationResponse.builder()
                    .loggedIn(false)
                    .build();
            return ResponseEntity.ok(response);
        }

        User user = currentUser.get();
        User cloneUser = User.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .avatar(createThumbnailUrl(user.getAvatar(), user.getId()))
                .apps(user.getApps())
                .buildWithoutPassword();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .user(cloneUser)
                .loggedIn(true)
                .apps(cloneUser.getApps())
                .build();

        return ResponseEntity.ok(response);
    }

    private String createThumbnailUrl(String filename, int userId) {
        return "https://stage-app-profiles-germany.s3.amazonaws.com/user/" + userId + "/avatar/thumbnail/" + filename;
    }


}
