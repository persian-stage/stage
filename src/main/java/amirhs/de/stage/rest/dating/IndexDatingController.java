package amirhs.de.stage.rest.dating;


import amirhs.de.stage.service.UserService;
import amirhs.de.stage.user.App;
import amirhs.de.stage.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/v1/dating")
public class IndexDatingController {

    UserService userService;

    public IndexDatingController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> index(@AuthenticationPrincipal UserDetails user) {
        List<App> apps = this.userService.getAppsByUser((User) user);
        boolean hasDatingApp =  apps.stream().anyMatch(app -> "dating".equals(app.getName()));

        if (!hasDatingApp) {
            Map<String, String> response = new HashMap<>();
            response.put("redirectUrl", "/register");
            response.put("status", HttpStatus.TEMPORARY_REDIRECT.value() + "");
            return ResponseEntity.ok(response);
        }

        Map<String, String> response = new HashMap<>();
        response.put("data", "Your additional data here");
        response.put("status", HttpStatus.OK.value() + "");

        return ResponseEntity.ok(response);
    }
}
