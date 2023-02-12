package dev.amir.resourceservice.application.manager;

import dev.amir.resourceservice.application.port.output.ResourceMessageProducerOutputPort;
import dev.amir.resourceservice.application.retry.service.RetryExecutorService;
import dev.amir.resourceservice.domain.entity.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceMessageManagerImpl implements ResourceMessageManager {
    private final ResourceMessageProducerOutputPort resourceMessageProducerOutputPort;
    private final RetryExecutorService retryExecutorService;

    @Override
    public void sendProcessResourceMessage(Resource resource) {
        retryExecutorService.executeAndRecover(() -> resourceMessageProducerOutputPort.sendProcessResourceMessage(resource));
    }
}
