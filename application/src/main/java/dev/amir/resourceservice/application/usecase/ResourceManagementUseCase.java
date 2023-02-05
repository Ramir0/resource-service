package dev.amir.resourceservice.application.usecase;

import dev.amir.resourceservice.domain.entity.Resource;

import java.util.Collection;

public interface ResourceManagementUseCase {
    Resource createResource(byte[] resourceData);

    Resource getResourceById(Long resourceId);

    byte[] getResourceData(Resource resource);

    byte[] getPartialResourceData(Resource resource, long start, long end);

    Collection<Long> deleteResourceById(Collection<Long> ids);
}
