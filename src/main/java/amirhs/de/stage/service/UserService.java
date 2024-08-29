package amirhs.de.stage.service;

import amirhs.de.stage.user.App;
import amirhs.de.stage.user.AppRepository;
import amirhs.de.stage.user.User;
import amirhs.de.stage.user.UserRepository;
import org.springframework.stereotype.Service;
import amirhs.de.stage.user.Status;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Optional<User> getUserWithEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Transactional
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

//    public void saveUserStatus(User user) {
//        user.setStatus(Status.ONLINE);
//        userRepository.save(user);
//    }

    public void disconnect(User user) {
        User storedUser = userRepository.findById(user.getId()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            userRepository.save(storedUser);
        }
    }

    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(Status.ONLINE);
    }
}
