package dev.amir.resourceservice.domain.exception;

public class InvalidResourceException extends RuntimeException {
    public InvalidResourceException(String message) {
        super(message);
    }
    public InvalidResourceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
