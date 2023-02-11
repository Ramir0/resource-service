package dev.amir.resourceservice.application.retry;

@FunctionalInterface
public interface RetryCallback {
    void execute();
}
