package dev.amir.resourceservice.framework.output.sql.mapper;

import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.vo.ContentLength;
import dev.amir.resourceservice.domain.vo.ContentType;
import dev.amir.resourceservice.domain.vo.ResourceName;
import dev.amir.resourceservice.framework.output.sql.entity.ResourceJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResourceEntityMapper {
    @Mapping(target = "name", expression = "java(mapToResourceName(entity.getName()))")
    @Mapping(target = "contentType", expression = "java(mapToContentType(entity.getContentType()))")
    @Mapping(target = "contentLength", expression = "java(mapToContentLength(entity.getContentLength()))")
    Resource convert(ResourceJpaEntity entity);

    @Mapping(target = "name", expression = "java(mapToResourceName(resource.getName()))")
    @Mapping(target = "contentType", expression = "java(mapToContentType(resource.getContentType()))")
    @Mapping(target = "contentLength", expression = "java(mapToContentLength(resource.getContentLength()))")
    ResourceJpaEntity convert(Resource resource);

    default ResourceName mapToResourceName(String name) {
        return ResourceName.getInstance(name);
    }

    default String mapToResourceName(ResourceName resourceName) {
        return resourceName.getValue();
    }

    default ContentType mapToContentType(String contentType) {
        return new ContentType(contentType);
    }

    default String mapToContentType(ContentType contentType) {
        return contentType.getValue();
    }

    default ContentLength mapToContentLength(Long contentLength) {
        return new ContentLength(contentLength);
    }

    default Long mapToContentLength(ContentLength contentLength) {
        return contentLength.getValue();
    }
}
