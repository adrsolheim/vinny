package no.vinny.nightfly.util.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends EntityNotFoundException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
