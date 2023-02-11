package dev.amir.resourceservice.framework.output.kafka;

import dev.amir.resourceservice.application.port.output.ResourceMessageProducerOutputPort;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.exception.UnexpectedResourceException;
import dev.amir.resourceservice.framework.output.kafka.message.ProcessResourceMessage;
import dev.amir.resourceservice.framework.output.kafka.producer.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceMessageProducerKafkaAdapter implements ResourceMessageProducerOutputPort {
    private final MessageProducer messageProducer;

    @Value("${spring.kafka.topics.resource-process}")
    private String topicResourceProcess;

    @Override
    public void sendProcessResourceMessage(Resource resource) {
        try {
            var processResourceMessage = new ProcessResourceMessage(resource.getId());
            messageProducer.send(topicResourceProcess, processResourceMessage);
        } catch (Exception exception) {
            throw new UnexpectedResourceException(exception);
        }
    }
}
