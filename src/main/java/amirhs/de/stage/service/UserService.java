package amirhs.de.stage.service;

import amirhs.de.stage.dto.UserDTO;
import amirhs.de.stage.user.App;
import amirhs.de.stage.user.AppRepository;
import amirhs.de.stage.user.User;
import amirhs.de.stage.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private final AppRepository appRepository;

    public UserService(UserRepository userRepository, AppRepository appRepository) {
        this.userRepository = userRepository;
        this.appRepository = appRepository;
    }

    public Optional<UserDTO> getUser(String username) {
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstname(user.getFirstname());
            userDTO.setLastname(user.getLastname());
            userDTO.setEmail(user.getEmail());
            userDTO.setAvatar(user.getImage());
            userDTO.setRole(user.getRole());
            return Optional.of(userDTO);
        } else {
            return Optional.empty();
        }
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public List<App> getAppsByUser(User user) {
        return appRepository.findByUser(user);
    }

    public App addAppToUser(User user, App app) {
        app.setUser(user);
        return appRepository.save(app);
    }

    public void removeAppFromUser(User user, App app) {
        app.setUser(null);
        appRepository.delete(app);
    }
}
