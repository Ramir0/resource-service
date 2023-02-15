package dev.amir.resourceservice.application.retry.service;

import dev.amir.resourceservice.application.retry.RetryCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetryExecutorServiceImpl implements RetryExecutorService {
    private final RetryTemplate retryTemplate;

    @Override
    public void execute(RetryCallback callback) {
        retryTemplate.execute(retryContext -> {
            callback.execute();
            return null;
        });
    }
}
