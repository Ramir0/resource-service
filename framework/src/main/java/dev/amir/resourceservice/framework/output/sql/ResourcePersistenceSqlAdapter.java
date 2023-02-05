package dev.amir.resourceservice.framework.output.sql;

import dev.amir.resourceservice.application.port.output.ResourcePersistenceOutputPort;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.framework.output.sql.mapper.ResourceEntityMapper;
import dev.amir.resourceservice.framework.output.sql.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourcePersistenceSqlAdapter implements ResourcePersistenceOutputPort {
    private final ResourceRepository resourceRepository;
    private final ResourceEntityMapper mapper;

    @Override
    public Resource saveResource(Resource resource) {
        var resourceEntity = resourceRepository.save(Objects.requireNonNull(mapper.convert(resource)));
        return Objects.requireNonNull(mapper.convert(resourceEntity));
    }

    @Override
    public Optional<Resource> getResourceById(Long resourceId) {
        return resourceRepository.findById(resourceId).map(mapper::convert);
    }

    @Override
    public Collection<Resource> getAllResourceById(Collection<Long> resourceIds) {
        return resourceRepository.findAllByIdIn(resourceIds).stream().map(mapper::convert).collect(Collectors.toList());
    }

    @Override
    public void deleteResourceById(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        resourceRepository.deleteAllById(ids);
    }
}
