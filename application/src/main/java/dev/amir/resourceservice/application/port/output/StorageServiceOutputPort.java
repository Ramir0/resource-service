package dev.amir.resourceservice.application.port.output;

import dev.amir.resourceservice.application.dto.StorageInformation;

import java.util.Collection;

public interface StorageServiceOutputPort {
    Collection<StorageInformation> getAllStorageInformation();
}
