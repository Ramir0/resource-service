package dev.amir.resourceservice.framework.output.rest.client;

import dev.amir.resourceservice.framework.output.rest.response.GetStorageResponse;

import java.util.Collection;

public interface StorageServiceRestClient {
    Collection<GetStorageResponse> getAllStorages();
}
