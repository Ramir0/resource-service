package dev.amir.resourceservice.framework.output.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.amir.resourceservice.framework.output.kafka.message.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(String topic, KafkaMessage payload) throws JsonProcessingException {
        var payloadAsJson = objectMapper.writeValueAsString(payload);
        log.info("Sending message - topic: [{}] message: [{}]", topic, payloadAsJson);
        kafkaTemplate.send(topic, payloadAsJson);
    }
}
