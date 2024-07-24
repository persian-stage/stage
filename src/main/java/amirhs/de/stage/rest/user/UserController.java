package amirhs.de.stage.rest.user;

import amirhs.de.stage.user.User;
import amirhs.de.stage.util.UserContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> getUser() {

            User user = UserContextHolder.getCurrentUser();

        Map<String, String> response = new HashMap<>();
        response.put("id", user.getId() + "");
        response.put("firstname", user.getFirstname());
        response.put("lastname", user.getLastname());
        response.put("email", user.getEmail());
        response.put("avatar", user.getImage());

        return ResponseEntity.ok().body(response);
    }

}
