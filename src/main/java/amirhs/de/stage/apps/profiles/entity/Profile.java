package amirhs.de.stage.apps.profiles.entity;

import amirhs.de.stage.user.App;
import amirhs.de.stage.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "app_profiles_profile")
@JsonIgnoreProperties({"apps"})
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lookingForwardToGender;

    @OneToOne
    @JoinColumn(name = "_user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<App> apps = new ArrayList<>();

    public void profile() {}

    public void profile(String lookingForwardToGender, User user) {
        this.lookingForwardToGender = lookingForwardToGender;
        this.user = user;
    }

    public String getLookingForwardToGender() {
        return lookingForwardToGender;
    }

    public void setLookingForwardToGender(String lookingForwardToGender) {
        this.lookingForwardToGender = lookingForwardToGender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id) && Objects.equals(lookingForwardToGender, profile.lookingForwardToGender) && Objects.equals(user, profile.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lookingForwardToGender, user);
    }

    @Override
    public String toString() {
        return "profile{" +
                "lookingForwardToGender='" + lookingForwardToGender + '\'' +
                ", user=" + user +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
