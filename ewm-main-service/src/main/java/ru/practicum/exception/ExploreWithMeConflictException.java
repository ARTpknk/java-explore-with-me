package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)

public class ExploreWithMeConflictException extends RuntimeException {
    public ExploreWithMeConflictException(String message) {
        super(message);
    }
}