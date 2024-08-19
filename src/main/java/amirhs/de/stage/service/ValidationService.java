package amirhs.de.stage.service;

import amirhs.de.stage.common.ResponseErrorMessage;
import amirhs.de.stage.user.UserRepository;
import amirhs.de.stage.auth.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    private final UserRepository userRepository;

    public ValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<ResponseErrorMessage> validateRegisterRequest(RegisterRequest request) {
        List<ResponseErrorMessage> errorMessages = new ArrayList<>();

        errorMessages.addAll(validateEmail(request.getEmail()));
        errorMessages.addAll(validateFirstname(request.getFirstname()));
        errorMessages.addAll(validateLastname(request.getLastname()));
        errorMessages.addAll(validatePassword(request.getPassword()));
        errorMessages.addAll(validateEmailExists(request.getEmail()));

        return errorMessages;
    }

    public List<ResponseErrorMessage> validateEmail(String email) {
        List<ResponseErrorMessage> errorMessages = new ArrayList<>();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errorMessages.add(ResponseErrorMessage.builder()
                    .form("register")
                    .field("email")
                    .message("Invalid email format")
                    .buildWithFormAndField());
        }
        return errorMessages;
    }

    public List<ResponseErrorMessage> validateFirstname(String firstname) {
        List<ResponseErrorMessage> errorMessages = new ArrayList<>();
        if (!NAME_PATTERN.matcher(firstname).matches()) {
            errorMessages.add(ResponseErrorMessage.builder()
                    .form("register")
                    .field("firstname")
                    .message("Invalid firstname format")
                    .buildWithFormAndField());
        }
        return errorMessages;
    }

    public List<ResponseErrorMessage> validateLastname(String lastname) {
        List<ResponseErrorMessage> errorMessages = new ArrayList<>();
        if (!NAME_PATTERN.matcher(lastname).matches()) {
            errorMessages.add(ResponseErrorMessage.builder()
                    .form("register")
                    .field("lastname")
                    .message("Invalid lastname format")
                    .buildWithFormAndField());
        }
        return errorMessages;
    }

    public List<ResponseErrorMessage> validatePassword(String password) {
        List<ResponseErrorMessage> errorMessages = new ArrayList<>();
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errorMessages.add(ResponseErrorMessage.builder()
                    .form("register")
                    .field("password")
                    .message("Password must be at least 8 characters long, contain at least one digit, one lowercase letter, and one uppercase letter")
                    .buildWithFormAndField());
        }
        return errorMessages;
    }

    public List<ResponseErrorMessage> validateEmailExists(String email) {
        List<ResponseErrorMessage> errorMessages = new ArrayList<>();
        if (userRepository.findByEmail(email).isPresent()) {
            errorMessages.add(ResponseErrorMessage.builder()
                    .form("register")
                    .field("email")
                    .message("Email already exists")
                    .buildWithFormAndField());
        }
        return errorMessages;
    }
}