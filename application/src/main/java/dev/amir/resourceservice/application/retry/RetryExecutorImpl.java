package dev.amir.resourceservice.application.retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetryExecutorImpl implements RetryExecutor {
    private final RetryTemplate retryTemplate;

    @Override
    public void execute(RetryAction callback) {
        retryTemplate.execute(retryContext -> {
            if (retryContext.getRetryCount() > 0) {
                log.warn("Retry count: [{}] Error message: [{}]", retryContext.getRetryCount(), retryContext.getLastThrowable().getMessage());
            }
            callback.execute();
            return null;
        });
    }
}
