package amirhs.de.stage.common;

import java.util.Objects;

public class ResponseErrorMessage {

    private String errorMessage;
    private String field;
    private String form;

    public ResponseErrorMessage(String errorMessage, String field, String form) {
        this.errorMessage = errorMessage;
        this.field = field;
        this.form = form;
    }

    public ResponseErrorMessage(String errorMessage, String field) {
        this.errorMessage = errorMessage;
        this.field = field;
    }

    public ResponseErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResponseErrorMessage() {
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ResponseMessageBuilder builder() {
        return new ResponseMessageBuilder();
    }

    @Override
    public String toString() {
        return "ResponseErrorMessage{" +
                "errorMessage='" + errorMessage + '\'' +
                ", field='" + field + '\'' +
                ", form='" + form + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseErrorMessage that = (ResponseErrorMessage) o;
        return Objects.equals(errorMessage, that.errorMessage) && Objects.equals(field, that.field) && Objects.equals(form, that.form);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage, field, form);
    }

    public static class ResponseMessageBuilder {
        private String errorMessage;
        private String field;
        private String form;

        ResponseMessageBuilder() {
        }

        public ResponseMessageBuilder form(String errorMessage) {
            this.form = errorMessage;
            return this;
        }

        public ResponseMessageBuilder field(String errorMessage) {
            this.field = errorMessage;
            return this;
        }

        public ResponseMessageBuilder message(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public ResponseErrorMessage build() {
            return new ResponseErrorMessage(errorMessage);
        }

        public ResponseErrorMessage buildWithFormAndField() {
            return new ResponseErrorMessage(errorMessage, field, form);
        }

        @Override
        public String toString() {
            return "ResponseMessageBuilder{" +
                    "errorMessage='" + errorMessage + '\'' +
                    ", field='" + field + '\'' +
                    ", form='" + form + '\'' +
                    '}';
        }
    }
}