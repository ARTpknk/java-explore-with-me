package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ExploreWithMeNotFoundException extends RuntimeException {
    public ExploreWithMeNotFoundException(String message) {
        super(message);
    }
}