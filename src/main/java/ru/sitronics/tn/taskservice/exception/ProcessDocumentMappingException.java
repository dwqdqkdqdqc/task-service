package ru.sitronics.tn.taskservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ProcessDocumentMappingException extends RuntimeException {
    public ProcessDocumentMappingException(String message) {
        super(message);
    }
}
