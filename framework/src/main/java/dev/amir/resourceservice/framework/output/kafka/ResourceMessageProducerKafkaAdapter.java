package dev.amir.resourceservice.framework.output.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.amir.resourceservice.application.port.output.ResourceMessageProducerOutputPort;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.exception.UnexpectedResourceException;
import dev.amir.resourceservice.framework.output.kafka.message.ProcessResourceMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceMessageProducerKafkaAdapter implements ResourceMessageProducerOutputPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.resource-process}")
    private String topicResourceProcess;

    @Override
    public void sendProcessResourceMessage(Resource resource) {
        try {
            var payload = new ProcessResourceMessage(resource.getId());
            var payloadAsJson = objectMapper.writeValueAsString(payload);
            log.info("Sending message - topic: [{}] message: [{}]", topicResourceProcess, payloadAsJson);
            kafkaTemplate.send(topicResourceProcess, payloadAsJson);
        } catch (Exception exception) {
            throw new UnexpectedResourceException(exception);
        }
    }
}
