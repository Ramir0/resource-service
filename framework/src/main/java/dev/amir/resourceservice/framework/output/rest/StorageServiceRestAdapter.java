package dev.amir.resourceservice.framework.output.rest;

import dev.amir.resourceservice.application.dto.StorageInformation;
import dev.amir.resourceservice.application.port.output.StorageServiceOutputPort;
import dev.amir.resourceservice.framework.circuitbreaker.StorageCircuitBreaker;
import dev.amir.resourceservice.framework.output.rest.client.StorageServiceRestClient;
import dev.amir.resourceservice.framework.output.rest.mapper.GetStorageResponseMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageServiceRestAdapter implements StorageServiceOutputPort {
    private final StorageServiceRestClient storageServiceRestClient;
    private final GetStorageResponseMapper getStorageResponseMapper;
    private final StorageCircuitBreaker storageCircuitBreaker;

    @Override
    @CircuitBreaker(name = "storage-service", fallbackMethod = "fallbackForGetStorage")
    public Collection<StorageInformation> getAllStorageInformation() {
        return getStorageResponseMapper.convertCollection(storageServiceRestClient.getAllStorages());
    }

    public Collection<StorageInformation> fallbackForGetStorage(Exception exception) {
        return storageCircuitBreaker.fallbackForGetStorage(exception);
    }
}
