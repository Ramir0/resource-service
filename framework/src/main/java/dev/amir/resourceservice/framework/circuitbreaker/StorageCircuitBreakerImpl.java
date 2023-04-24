package dev.amir.resourceservice.framework.circuitbreaker;

import dev.amir.resourceservice.application.dto.StorageInformation;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class StorageCircuitBreakerImpl implements StorageCircuitBreaker {
    @Value("${services.storage-service.type.temporal}")
    private String temporalStorageType;

    @Value("${services.storage-service.type.permanent}")
    private String permanentStorageType;

    private Collection<StorageInformation> stubStorageResult;

    @PostConstruct
    public void initializeStubStorage() {
        var permanentStorage = StorageInformation.builder()
                .id(1L)
                .storageType(permanentStorageType)
                .bucket("resource-permanent-bucket")
                .path("resources")
                .build();
        var temporalStorage = StorageInformation.builder()
                .id(2L)
                .storageType(temporalStorageType)
                .bucket("resource-staging-bucket")
                .path("resources")
                .build();

        stubStorageResult = List.of(temporalStorage, permanentStorage);
    }

    @Override
    public Collection<StorageInformation> fallbackForGetStorage(Exception exception) {
        log.error("CircuitBreaker - Error occurred while getting all StorageInformation", exception);
        return stubStorageResult;
    }
}
