package dev.amir.resourceservice.application.port.output;

import dev.amir.resourceservice.domain.entity.Resource;

import java.util.Collection;
import java.util.Optional;

public interface ResourcePersistenceOutputPort {
    Resource saveResource(Resource resource);

    Optional<Resource> getResourceById(Long resourceId);
    Collection<Resource> getAllResourceById(Collection<Long> resourceIds);

    void deleteResourceById(Collection<Long> ids);
}
