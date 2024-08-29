package amirhs.de.stage.auth;

import amirhs.de.stage.config.CookieHandler;
import amirhs.de.stage.config.JwtService;
import amirhs.de.stage.common.ResponseErrorMessage;
import amirhs.de.stage.service.ValidationService;
import amirhs.de.stage.user.Role;
import amirhs.de.stage.user.Status;
import amirhs.de.stage.user.User;
import amirhs.de.stage.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieHandler cookieHandler;
    private final ValidationService validationService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            CookieHandler cookieHandler,
            ValidationService validationService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.cookieHandler = cookieHandler;
        this.validationService = validationService;
    }

    public AuthenticationResponse register(RegisterRequest request) {

        List<ResponseErrorMessage> errorMessages = validationService.validateRegisterRequest(request);
        if (!errorMessages.isEmpty()) {
            return AuthenticationResponse.builder()
                    .formErrorMessages(errorMessages)
                    .build();
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(Status.OFFLINE)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).user(user).loggedIn(true).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        User cloneUser = User.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .build();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(cloneUser)
                .build();
    }

    public void invalidateToken(HttpServletRequest request) {
        final String jwt = cookieHandler.getJwtFromCookies(request);
        if (jwt == null) return;
        jwtService.invalidateToken(jwt);
    }
}
