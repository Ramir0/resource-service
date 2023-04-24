package dev.amir.resourceservice.application.service;

import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;

import java.util.Collection;

public interface ResourceDataStorageService {
    void uploadResource(Resource resourceId, byte[] resourceData);

    byte[] downloadResource(Resource resource);

    byte[] downloadResource(Resource resource, ByteRange byteRange);

    void deleteResources(Collection<Resource> resources);

    void moveResource(Resource resource);
}
