package dev.amir.resourceservice.framework.output.kafka.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.delivery.timeout}")
    private String deliveryTimeoutInMills;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        var producerFactory = new DefaultKafkaProducerFactory<String, String>(producerConfigs());
        return new KafkaTemplate<>(producerFactory);
    }

    private Map<String, Object> producerConfigs() {
        var properties = new HashMap<String, Object>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.RETRIES_CONFIG, 1);
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutInMills);
        properties.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, deliveryTimeoutInMills);
        properties.put(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, deliveryTimeoutInMills);
        return properties;
    }
}
