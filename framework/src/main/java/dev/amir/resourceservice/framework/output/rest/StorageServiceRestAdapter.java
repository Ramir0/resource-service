package dev.amir.resourceservice.framework.output.rest;

import dev.amir.resourceservice.application.dto.StorageInformation;
import dev.amir.resourceservice.application.port.output.StorageServiceOutputPort;
import dev.amir.resourceservice.framework.output.rest.client.StorageServiceRestClient;
import dev.amir.resourceservice.framework.output.rest.mapper.GetStorageResponseMapper;
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

    @Override
    public Collection<StorageInformation> getAllStorageInformation() {
        return getStorageResponseMapper.convertCollection(storageServiceRestClient.getAllStorages());
    }
}
