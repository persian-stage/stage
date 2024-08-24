package amirhs.de.stage.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import amirhs.de.stage.apps.profiles.entity.Profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "_user")
@JsonIgnoreProperties({"apps"})
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private String gender;
    private String email;
    private String password;
    private String avatar;
    private String country;
    private String city;
    private String username;

    @Embedded
    private DateOfBirth dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<App> apps = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public User(
            Integer id,
            String firstname,
            String lastname,
            String email,
            String password,
            Role role
    ) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(
            Integer id,
            String firstname,
            String lastname,
            String email,
            List<App> apps,
            Profile profile,
            String avatar) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.apps = apps;
        this.profile = profile;
        this.avatar = avatar;
    }

    public User() {
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
        return this.id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public Role getRole() {
        return this.role;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public void addApp(App app) {
        apps.add(app);
        app.setUser(this);
    }

    public void removeApp(App app) {
        apps.remove(app);
        app.setUser(null);
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateOfBirth dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$firstname = this.getFirstname();
        final Object other$firstname = other.getFirstname();
        if (this$firstname == null ? other$firstname != null : !this$firstname.equals(other$firstname)) return false;
        final Object this$lastname = this.getLastname();
        final Object other$lastname = other.getLastname();
        if (this$lastname == null ? other$lastname != null : !this$lastname.equals(other$lastname)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$role = this.getRole();
        final Object other$role = other.getRole();
        if (this$role == null ? other$role != null : !this$role.equals(other$role)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $firstname = this.getFirstname();
        result = result * PRIME + ($firstname == null ? 43 : $firstname.hashCode());
        final Object $lastname = this.getLastname();
        result = result * PRIME + ($lastname == null ? 43 : $lastname.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $role = this.getRole();
        result = result * PRIME + ($role == null ? 43 : $role.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", role=" + role +
                ", apps=" + apps +
                ", profile=" + profile +
                '}';
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static class UserBuilder {
        private Integer id;
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        private Role role;
        private List<App> apps;
        private Profile profile;
        private String avatar;

        UserBuilder() {
        }

        public UserBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public UserBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder apps(List<App> apps) {
            this.apps = apps;
            return this;
        }

        public UserBuilder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public UserBuilder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public User build() {
            return new User(this.id, this.firstname, this.lastname, this.email, this.password, this.role);
        }

        public User buildWithoutPassword() {
            return new User(this.id, this.firstname, this.lastname, this.email, this.apps, this.profile, this.avatar);
        }

        @Override
        public String toString() {
            return "UserBuilder{" +
                    "id=" + id +
                    ", firstname='" + firstname + '\'' +
                    ", lastname='" + lastname + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", role=" + role +
                    '}';
        }
    }
}