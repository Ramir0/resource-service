package dev.amir.resourceservice.application.manager;

import dev.amir.resourceservice.application.port.output.ResourceMessageProducerOutputPort;
import dev.amir.resourceservice.application.retry.RetryExecutor;
import dev.amir.resourceservice.domain.entity.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceMessageManagerImpl implements ResourceMessageManager {
    private final ResourceMessageProducerOutputPort resourceMessageProducerOutputPort;
    private final RetryExecutor retryExecutor;

    @Override
    public void sendProcessResourceMessage(Resource resource) {
        retryExecutor.execute(() -> resourceMessageProducerOutputPort.sendProcessResourceMessage(resource));
    }
}
