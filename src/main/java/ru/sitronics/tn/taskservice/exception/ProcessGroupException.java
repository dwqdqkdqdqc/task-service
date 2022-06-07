package ru.sitronics.tn.taskservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ProcessGroupException extends RuntimeException {

    public ProcessGroupException(String message) {
        super(message);
    }

    public ProcessGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
