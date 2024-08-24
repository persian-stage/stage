package amirhs.de.stage.rest;

import amirhs.de.stage.utils.UserContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class IndexController {

    @GetMapping("/index")
    public ResponseEntity<Map<String, String>> indexAction() {

        String email = UserContextHolder.getCurrentUsername();

        Map<String, String> response = new HashMap<>();
        response.put("message", "User email: " + email);
        response.put("path", "/");

        return ResponseEntity.ok().body(response);
    }

}
