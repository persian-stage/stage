package amirhs.de.stage.rest.user;

import amirhs.de.stage.auth.AuthenticationResponse;
import amirhs.de.stage.service.UserService;
import amirhs.de.stage.user.User;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
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
                .gender(user.getGender())
                .buildWithoutPassword();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .user(cloneUser)
                .loggedIn(true)
                .apps(cloneUser.getApps())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok().body(user);
    }

    private String createThumbnailUrl(String filename, int userId) {
        return "https://stage-app-profiles-germany.s3.amazonaws.com/user/" + userId + "/avatar/thumbnail/" + filename;
    }

    @GetMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteAccount(
            @AuthenticationPrincipal UserDetails UserDetails
    ) {
        Optional<User> currentUser = userService.getUserWithEmail(UserDetails.getUsername());

        if (currentUser.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            response.put("status", "403");
            return ResponseEntity.ok(response);
        }

        if (userService.deleteUser(UserDetails.getUsername())) {

            ResponseCookie cookie = ResponseCookie.from("token", "")
            .httpOnly(true)
            .secure(false) // todo set condition for dev and prod value
            .path("/")
            .maxAge(0) // Set maxAge to 0 to delete the cookie
            .sameSite("Strict")
            .build();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Delete Account successful");
            response.put("status", "200");

            return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete Account unsuccessful");
        response.put("status", "403");

        return ResponseEntity.ok().body(response);
    }

}
