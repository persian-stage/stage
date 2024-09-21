package amirhs.de.stage.apps.profiles.dto;

import amirhs.de.stage.apps.profiles.entity.Image;
import amirhs.de.stage.user.DateOfBirth;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class UserProfileDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private String country;
    private DateOfBirth dateOfBirth;
    private String username;
    private String avatar;
    private String gender;
    private List<Image> images = new ArrayList<>();
}