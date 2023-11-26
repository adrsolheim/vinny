package no.vinny.nightfly.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class OperationFailedException extends RuntimeException {

    public OperationFailedException(String message) {
        super(message);
    }
}
