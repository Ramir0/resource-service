package dev.amir.resourceservice.application.retry.service;

import dev.amir.resourceservice.application.retry.RetryCallback;

public interface RetryExecutorService {
    void executeAndRecover(RetryCallback callback);
}
