package dev.amir.resourceservice.framework.output.kafka.message;

public record ProcessResourceMessage(long id) implements KafkaMessage {
}
