package amirhs.de.stage.auth;

import amirhs.de.stage.common.ResponseErrorMessage;
import amirhs.de.stage.user.App;
import amirhs.de.stage.user.User;

import java.util.List;
import java.util.Objects;

public class AuthenticationResponse {

    private String token;
    private List<ResponseErrorMessage> formErrors;
    private User user;private boolean loggedIn;
    private List<App> apps;

    public AuthenticationResponse() {
    }

    public static AuthenticationResponseBuilder builder() {
        return new AuthenticationResponseBuilder();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public List<ResponseErrorMessage> getFormErrors() {
        return formErrors;
    }

    public void setFormErrors(List<ResponseErrorMessage> errorMessage) {
        this.formErrors = errorMessage;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "token='" + token + '\'' +
                ", errorMessages=" + formErrors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationResponse that = (AuthenticationResponse) o;
        return Objects.equals(token, that.token) && Objects.equals(formErrors, that.formErrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, formErrors);
    }

    public static class AuthenticationResponseBuilder {
        private String token;
        private List<ResponseErrorMessage> formErrorMessages;
        private User user;
        private boolean loggedIn;
        private List<App> apps;

        AuthenticationResponseBuilder() {}

        public AuthenticationResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationResponseBuilder formErrorMessages(List<ResponseErrorMessage> formErrorMessages) {
            this.formErrorMessages = formErrorMessages;
            return this;
        }

        public AuthenticationResponseBuilder user(User user) {
            this.user = user;
            return this;
        }

        public AuthenticationResponseBuilder loggedIn(boolean loggedIn) {
            this.loggedIn = loggedIn;
            return this;
        }

        public AuthenticationResponseBuilder apps(List<App> apps) {
            this.apps = apps;
            return this;
        }

        public AuthenticationResponse build() {
            AuthenticationResponse response = new AuthenticationResponse();
            response.setToken(token);
            response.setFormErrors(formErrorMessages);
            response.setUser(user);
            response.setLoggedIn(loggedIn);
            response.setApps(apps);
            return response;
        }

        @Override
        public String toString() {
            return "AuthenticationResponseBuilder{" +
                    "token='" + token + '\'' +
                    ", errorMessages=" + formErrorMessages +
                    '}';
        }
    }
}
