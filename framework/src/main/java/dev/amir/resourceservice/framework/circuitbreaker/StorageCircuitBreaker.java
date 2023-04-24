package dev.amir.resourceservice.framework.circuitbreaker;

import dev.amir.resourceservice.application.dto.StorageInformation;

import java.util.Collection;

public interface StorageCircuitBreaker {
    Collection<StorageInformation> fallbackForGetStorage(Exception exception);
}
