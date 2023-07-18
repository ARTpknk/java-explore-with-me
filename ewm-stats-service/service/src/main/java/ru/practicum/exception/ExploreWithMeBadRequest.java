package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ExploreWithMeBadRequest extends RuntimeException {
    public ExploreWithMeBadRequest(String message) {
        super(message);
    }
}
