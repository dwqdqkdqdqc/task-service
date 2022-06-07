package ru.sitronics.tn.taskservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, code = HttpStatus.INTERNAL_SERVER_ERROR)
public class BpmEngineException extends RuntimeException {
    public BpmEngineException(String message) {
        super(message);
    }
}
