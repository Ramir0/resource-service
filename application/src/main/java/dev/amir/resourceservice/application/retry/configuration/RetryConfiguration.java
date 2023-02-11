package dev.amir.resourceservice.application.retry.configuration;

import dev.amir.resourceservice.domain.exception.UnexpectedResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Slf4j
@Configuration
public class RetryConfiguration {

    @Value("${spring.retry.maxAttempts}")
    private Integer maxAttempts;

    @Value("${spring.retry.maxDelayInMillis}")
    private Long maxDelayInMillis;

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(maxAttempts)
                .fixedBackoff(maxDelayInMillis)
                .retryOn(UnexpectedResourceException.class)
                .build();
    }
}
