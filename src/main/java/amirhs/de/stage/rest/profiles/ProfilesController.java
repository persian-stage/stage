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

    @GetMapping("/profileCards")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getProfileCards(@AuthenticationPrincipal UserDetails user) {
        Optional<User> currentUser = userService.getUserWithEmail(user.getUsername());

        int userId;

        if(currentUser.isPresent()) {
            userId = currentUser.get().getId();
        } else {
            return ResponseEntity.notFound().build();
        }

        List<App> apps = this.userService.getAppsByUser((User) user);
        boolean hasProfilesApp =  apps.stream().anyMatch(app -> "profiles".equals(app.getName()));

//        if (!hasProfilesApp || currentUser.isEmpty()) {
//            Map<String, String> response = new HashMap<>();
//            response.put("redirectUrl", "/register");
//            response.put("status", HttpStatus.TEMPORARY_REDIRECT.value() + "");
//            return ResponseEntity.ok(response);
//        }

        List<Profile> profiles = this.ProfileService.getAllProfiles();

        List<Map<String, Object>> profileData = new ArrayList<>();
        for (Profile profile : profiles) {
            if (profile.getUser().getId().equals(userId))
                continue;
            Map<String, Object> profileMap = new HashMap<>();
            profileMap.put("profile", profile);
            profileMap.put("user", profile.getUser());
            profileMap.put("mediumImage", createMediumImageUrl(profile.getUser().getAvatar(), profile.getUser().getId()));
            profileMap.put("apps", profile.getApps());
            profileData.add(profileMap);
        }

        Map<String, List<Map<String, Object>>> response = new HashMap<>();
        response.put("profiles", profileData);

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
        this.ProfileService.createProfile(profile);

        return getRedirectResponse();
    }

    private static @NotNull ResponseEntity<Map<String, String>> getRedirectResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", "/profiles");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String createMediumImageUrl(String filename, int userId) {
        return "https://stage-app-profiles-germany.s3.amazonaws.com/user/" + userId + "/avatar/medium/" + filename;
    }

}
