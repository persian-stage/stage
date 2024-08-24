package amirhs.de.stage.exception;

import amirhs.de.stage.common.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseWrapper> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        ResponseWrapper response = new ResponseWrapper()
                .add("message", "File size exceeds the maximum limit!")
                .add("status", HttpStatus.PAYLOAD_TOO_LARGE.value() + "");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }
}