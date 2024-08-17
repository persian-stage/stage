package amirhs.de.stage.apps.profiles;

import amirhs.de.stage.apps.profiles.entity.Profile;
import amirhs.de.stage.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}
