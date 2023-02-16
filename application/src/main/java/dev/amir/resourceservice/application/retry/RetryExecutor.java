package dev.amir.resourceservice.application.retry;

public interface RetryExecutor {
    void execute(RetryAction callback);
}
