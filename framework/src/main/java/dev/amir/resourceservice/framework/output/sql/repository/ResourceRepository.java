package dev.amir.resourceservice.framework.output.sql.repository;

import dev.amir.resourceservice.framework.output.sql.entity.ResourceJpaEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ResourceRepository extends CrudRepository<ResourceJpaEntity, Long> {
    Collection<ResourceJpaEntity> findAllByIdIn(Collection<Long> id);
}
