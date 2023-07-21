package ru.practicum.exception;

public class ExploreWithMeAlreadyExistsException extends RuntimeException {
    public ExploreWithMeAlreadyExistsException(String message) {
        super(message);
    }
}