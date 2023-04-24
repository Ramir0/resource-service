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
        var temporalStorage = StorageInformation.builder()
                .id(1L)
                .storageType(temporalStorageType)
                .bucket("temporal-bucket")
                .path("temporal/resource/path")
                .build();
        var permanentStorage = StorageInformation.builder()
                .id(2L)
                .storageType(permanentStorageType)
                .bucket("permanent-bucket")
                .path("permanent/resource/path")
                .build();

        stubStorageResult = List.of(temporalStorage, permanentStorage);
    }

    @Override
    public Collection<StorageInformation> fallbackForGetStorage(Exception exception) {
        log.error("CircuitBreaker - Error occurred while getting all StorageInformation", exception);
        return stubStorageResult;
    }
}
