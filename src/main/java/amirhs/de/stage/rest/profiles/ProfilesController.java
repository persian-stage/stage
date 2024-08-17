package amirhs.de.stage.rest.profiles;


import amirhs.de.stage.apps.profiles.entity.Profile;
import amirhs.de.stage.apps.profiles.service.ProfileService;
import amirhs.de.stage.service.UserService;
import amirhs.de.stage.user.App;
import amirhs.de.stage.user.DateOfBirth;
import amirhs.de.stage.user.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/app/profiles")
public class ProfilesController {

    UserService userService;
    private ProfileService ProfileService;

    public ProfilesController(UserService userService, amirhs.de.stage.apps.profiles.service.ProfileService profileService) {
        this.userService = userService;
        ProfileService = profileService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> getIndex(@AuthenticationPrincipal UserDetails user) {

        List<App> apps = this.userService.getAppsByUser((User) user);
        boolean hasProfilesApp =  apps.stream().anyMatch(app -> "profiles".equals(app.getName()));

        if (!hasProfilesApp) {
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

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterProfileRequest request, @AuthenticationPrincipal UserDetails user) {
        User currentUser = (User) user;

        List<App> apps = this.userService.getAppsByUser(currentUser);
        boolean hasProfilesApp =  apps.stream().anyMatch(app -> "profiles".equals(app.getName()));

        if (hasProfilesApp)
            return getRedirectResponse();

        List<App> profilesApp = List.of(new App("profiles", currentUser));

        DateOfBirth dateOfBirth = new DateOfBirth();
        dateOfBirth.setDay(request.getDateOfBirth().getDay());
        dateOfBirth.setMonth(request.getDateOfBirth().getMonth());
        dateOfBirth.setYear(request.getDateOfBirth().getYear());

        currentUser.setUsername(request.getProfileUsername());
        currentUser.setGender(request.getGender());
        currentUser.setDateOfBirth(dateOfBirth);
        currentUser.setCountry(request.getCountry());
        currentUser.setCity(request.getCity());
        currentUser.setApps(profilesApp);

        this.userService.updateUser(currentUser);

        Profile profile = new Profile();
        profile.setUser(currentUser);
        profile.setLookingForwardToGender(request.getLookingForwardToGender());
        this.ProfileService.createProfile(profile);

        return getRedirectResponse();
    }

    private static @NotNull ResponseEntity<Map<String, String>> getRedirectResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", "/profiles");
        return new ResponseEntity<>(response, HttpStatus.TEMPORARY_REDIRECT);
    }
}
