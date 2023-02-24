package dev.amir.resourceservice.application.usecase;

import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;

import java.util.Collection;

public interface ResourceManagementUseCase {
    Resource createResource(byte[] resourceData);

    Resource getResourceById(Long resourceId);

    byte[] getResourceData(Resource resource);

    byte[] getPartialResourceData(Resource resource, ByteRange byteRange);

    Collection<Long> deleteResourceById(Collection<Long> ids);
}
