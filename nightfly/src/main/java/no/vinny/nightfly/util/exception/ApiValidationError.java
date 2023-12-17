package no.vinny.nightfly.util.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiValidationError extends ApiErrorItem {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}
