package amirhs.de.stage.auth;

import amirhs.de.stage.common.ResponseErrorMessage;

import java.util.List;
import java.util.Objects;

public class AuthenticationResponse {

    private String token;
    private List<ResponseErrorMessage> formErrors;

    public AuthenticationResponse() {
    }

    public static AuthenticationResponseBuilder builder() {
        return new AuthenticationResponseBuilder();
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
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
        private List<ResponseErrorMessage> errorMessages;

        AuthenticationResponseBuilder() {}

        public AuthenticationResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationResponseBuilder errorMessages(List<ResponseErrorMessage> errorMessages) {
            this.errorMessages = errorMessages;
            return this;
        }

        public AuthenticationResponse build() {
            AuthenticationResponse response = new AuthenticationResponse();
            response.setToken(token);
            response.setFormErrors(errorMessages);
            return response;
        }

        @Override
        public String toString() {
            return "AuthenticationResponseBuilder{" +
                    "token='" + token + '\'' +
                    ", errorMessages=" + errorMessages +
                    '}';
        }
    }
}
