package dev.amir.resourceservice.domain.exception;

public class InvalidResourceException extends RuntimeException {
    public InvalidResourceException(String message) {
        super(message);
    }
}
