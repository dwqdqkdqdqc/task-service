package ru.sitronics.tn.taskservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST)
public class BpmValidationException extends RuntimeException {

    public BpmValidationException(String message) {
        super(message);
    }

    public BpmValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
