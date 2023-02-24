package dev.amir.resourceservice.application.retry;

import dev.amir.resourceservice.application.retry.exception.UnexpectedRetryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetryExecutorImpl implements RetryExecutor {
    private final RetryOperations retryTemplate;

    @Override
    public void execute(RetryAction callback) {
        try {
            retryTemplate.execute(retryContext -> {
                if (retryContext.getRetryCount() > 0) {
                    log.warn("Retry count: [{}] Error message: [{}]", retryContext.getRetryCount(), retryContext.getLastThrowable().getMessage());
                }
                callback.execute();
                return null;
            });
        } catch (Exception exception) {
            throw new UnexpectedRetryException(exception);
        }
    }
}
