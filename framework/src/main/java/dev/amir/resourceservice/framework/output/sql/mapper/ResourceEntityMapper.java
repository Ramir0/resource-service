package dev.amir.resourceservice.framework.output.sql.mapper;

import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.framework.output.sql.entity.ResourceJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResourceEntityMapper {
    Resource convert(ResourceJpaEntity entity);

    ResourceJpaEntity convert(Resource resource);
}
