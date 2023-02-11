package dev.amir.resourceservice.application.port.output;

import dev.amir.resourceservice.domain.entity.Resource;

import java.util.Collection;

public interface ResourceDataStorageOutputPort {
    void uploadResource(Resource resource, byte[] resourceData);

    byte[] downloadResource(Resource resource);

    byte[] downloadResource(Resource resource, long start, long end);

    void deleteResources(Collection<Resource> resources);
}
