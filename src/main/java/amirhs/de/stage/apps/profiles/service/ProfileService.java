package amirhs.de.stage.apps.profiles.service;

import amirhs.de.stage.apps.profiles.entity.Profile;
import amirhs.de.stage.apps.profiles.ProfileRepository;
import amirhs.de.stage.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public void createProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Transactional
    public Profile updateProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    @Transactional
    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    public Optional<Profile> getProfileByUser(User user) {
        return profileRepository.findByUser(user);
    }
}