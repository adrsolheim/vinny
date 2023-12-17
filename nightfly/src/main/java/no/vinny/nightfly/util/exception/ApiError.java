package no.vinny.nightfly.util.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiErrorItem> errors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, Throwable exception) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = exception.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable exception) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = exception.getLocalizedMessage();
    }
}
