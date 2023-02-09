package dev.amir.resourceservice.framework.output.sql.mapper;

import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.vo.ResourceName;
import dev.amir.resourceservice.framework.output.sql.entity.ResourceJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResourceEntityMapper {
    @Mapping(target = "name", expression = "java(mapToResourceName(entity.getName()))")
    Resource convert(ResourceJpaEntity entity);

    @Mapping(target = "name", expression = "java(mapToString(resource.getName()))")
    ResourceJpaEntity convert(Resource resource);

    default ResourceName mapToResourceName(String name) {
        return ResourceName.getInstance(name);
    }

    default String mapToString(ResourceName resourceName) {
        return resourceName.toString();
    }
}
