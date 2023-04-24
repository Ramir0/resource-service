package dev.amir.resourceservice.application.port.output;

import dev.amir.resourceservice.application.dto.StorageInformation;
import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;

import java.util.Collection;

public interface ResourceDataStorageOutputPort {
    void uploadResource(Resource resource, byte[] resourceData, StorageInformation storageInformation);

    byte[] downloadResource(Resource resource, StorageInformation storageInformation);

    byte[] downloadResource(Resource resource, ByteRange byteRange, StorageInformation storageInformation);

    void deleteResources(Collection<Resource> resources, StorageInformation storageInformation);

    void moveResource(Resource resource, StorageInformation sourceStorageInfo, StorageInformation destinationStorageInfo);
}
