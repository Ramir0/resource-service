package dev.amir.resourceservice.application.retry;

@FunctionalInterface
public interface RetryAction {
    void execute();
}
