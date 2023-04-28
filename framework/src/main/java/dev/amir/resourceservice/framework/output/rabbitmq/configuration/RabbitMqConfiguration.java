package dev.amir.resourceservice.framework.output.rabbitmq.configuration;

import dev.amir.resourceservice.domain.profile.Profiles;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!" + Profiles.TEST)
@Configuration(proxyBeanMethods = false)
public class RabbitMqConfiguration {
    @Value("${spring.rabbitmq.queues.resource-process}")
    private String resourceProcessQueue;

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cachingConnectionFactory) {
        var rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setObservationEnabled(true);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue createResourceProcessQueue() {
        return new Queue(resourceProcessQueue);
    }
}
