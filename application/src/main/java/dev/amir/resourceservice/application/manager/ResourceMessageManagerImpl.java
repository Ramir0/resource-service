package dev.amir.resourceservice.application.manager;

import dev.amir.resourceservice.application.port.output.ResourceMessageProducerOutputPort;
import dev.amir.resourceservice.application.retry.RetryExecutor;
import dev.amir.resourceservice.domain.entity.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceMessageManagerImpl implements ResourceMessageManager {
    private final ResourceMessageProducerOutputPort resourceMessageProducerOutputPort;
    private final RetryExecutor retryExecutor;

    @Override
    public void sendProcessResourceMessage(Resource resource) {
        log.info("Sending Resource with Id: [{}] to be processed", resource.getId());
        retryExecutor.execute(() -> resourceMessageProducerOutputPort.sendProcessResourceMessage(resource));
    }
}
