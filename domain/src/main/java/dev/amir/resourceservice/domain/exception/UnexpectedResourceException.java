package dev.amir.resourceservice.domain.exception;

public class UnexpectedResourceException extends RuntimeException {
    public UnexpectedResourceException(String message) {
        super(message);
    }

    public UnexpectedResourceException(Throwable throwable) {
        super(throwable);
    }

    public UnexpectedResourceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
