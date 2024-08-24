package amirhs.de.stage.user;

import amirhs.de.stage.apps.profiles.entity.Profile;
import jakarta.persistence.*;

@Entity
@Table(name = "apps")
public class App {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    // Constructors, getters, setters, equals, hashCode, toString methods

    public App() {}

    public App(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        App app = (App) o;

        return id != null ? id.equals(app.id) : app.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "App{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}