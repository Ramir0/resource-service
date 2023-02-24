package dev.amir.resourceservice.application.retry.exception;

public class UnexpectedRetryException extends RuntimeException {
    public UnexpectedRetryException(Throwable throwable) {
        super(throwable);
    }
}
