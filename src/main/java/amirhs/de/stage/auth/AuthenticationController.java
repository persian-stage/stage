package amirhs.de.stage.auth;

import amirhs.de.stage.utils.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    // TODO gic result auf service as AuthenticationResponse to body of ResponseEntity
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse res = authService.authenticate(request);
        ResponseCookie cookie = ResponseCookie.from("token", res.getToken())
                .httpOnly(true)
                .secure(false) // todo set condition for dev and prod value
                .path("/")
                .maxAge(7 * 23 * 60 * 60) // Less than 1 week
                .sameSite("Strict")
                .build();

        String email = UserContextHolder.getCurrentUsername();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("loggedIn", "true");
        response.put("email", email);

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            HttpServletRequest httpServletRequest
    ) {
        authService.invalidateToken(httpServletRequest);

        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // todo set condition for dev and prod value
                .path("/")
                .maxAge(0) // Set maxAge to 0 to delete the cookie
                .sameSite("Strict")
                .build();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        response.put("path", "/");

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
    }


}
