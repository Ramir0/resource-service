package dev.amir.resourceservice.application.port.output;

import dev.amir.resourceservice.domain.entity.Resource;

public interface ResourceMessageProducerOutputPort {
    void sendProcessResourceMessage(Resource resource);
}
