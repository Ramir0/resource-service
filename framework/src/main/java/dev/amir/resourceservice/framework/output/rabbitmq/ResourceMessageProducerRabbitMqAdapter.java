package dev.amir.resourceservice.framework.output.rabbitmq;

import dev.amir.resourceservice.application.port.output.ResourceMessageProducerOutputPort;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.framework.output.rabbitmq.message.ProcessResourceMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceMessageProducerRabbitMqAdapter implements ResourceMessageProducerOutputPort {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queues.resource-process}")
    private String resourceProcessQueue;

    @Override
    public void sendProcessResourceMessage(Resource resource) {
        var message = new ProcessResourceMessage(resource.getId());
        rabbitTemplate.convertAndSend(resourceProcessQueue, message);
        log.info("Message sent: {}", message);
    }
}
