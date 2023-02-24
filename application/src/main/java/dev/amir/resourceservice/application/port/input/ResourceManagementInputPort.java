package dev.amir.resourceservice.application.port.input;

import dev.amir.resourceservice.application.manager.ResourceMetaDataManager;
import dev.amir.resourceservice.application.port.output.ResourceDataStorageOutputPort;
import dev.amir.resourceservice.application.port.output.ResourceMessageProducerOutputPort;
import dev.amir.resourceservice.application.port.output.ResourcePersistenceOutputPort;
import dev.amir.resourceservice.application.retry.RetryExecutor;
import dev.amir.resourceservice.application.usecase.ResourceManagementUseCase;
import dev.amir.resourceservice.application.validator.ResourceMetadataValidator;
import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.exception.InvalidResourceException;
import dev.amir.resourceservice.domain.exception.ResourceNotFoundException;
import dev.amir.resourceservice.domain.vo.ContentLength;
import dev.amir.resourceservice.domain.vo.ContentType;
import dev.amir.resourceservice.domain.vo.ResourceName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceManagementInputPort implements ResourceManagementUseCase {
    private final ResourceMetaDataManager resourceMetaDataManager;
    private final ResourceMetadataValidator resourceMetadataValidator;
    private final ResourceDataStorageOutputPort resourceDataStorageOutputPort;
    private final ResourcePersistenceOutputPort resourcePersistenceOutputPort;
    private final ResourceMessageProducerOutputPort resourceMessageProducerOutputPort;
    private final RetryExecutor retryExecutor;

    @Override
    public Resource createResource(byte[] resourceData) {
        var contentType = resourceMetaDataManager.getContentType(resourceData);
        if (resourceMetadataValidator.isContentTypeInvalid(contentType)) {
            throw new InvalidResourceException("Invalid content type");
        }

        long contentLength = resourceMetaDataManager.getContentLength(resourceData);
        if (resourceMetadataValidator.isContentLengthInvalid(contentLength)) {
            throw new InvalidResourceException("Invalid content length");
        }

        log.info("Creating Resource with Content-Type: [{}] Content-Length: [{}]", contentType, contentLength);
        var newResource = buildResource(contentLength, contentType);
        resourceDataStorageOutputPort.uploadResource(newResource, resourceData);
        var savedResource = resourcePersistenceOutputPort.saveResource(newResource);
        retryExecutor.execute(() -> resourceMessageProducerOutputPort.sendProcessResourceMessage(savedResource));

        log.info("Resource with Id: [{}] was successfully created", savedResource.getId());
        return savedResource;
    }

    @Override
    public Resource getResourceById(Long resourceId) {
        return resourcePersistenceOutputPort.getResourceById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException(resourceId));
    }

    @Override
    public byte[] getResourceData(Resource resource) {
        log.info("Get ResourceData of Resource with Id [{}]", resource.getId());
        return resourceDataStorageOutputPort.downloadResource(resource);
    }

    @Override
    public byte[] getPartialResourceData(Resource resource, ByteRange byteRange) {
        log.info("Get partial [{}] ResourceData of Resource with Id [{}]", byteRange, resource.getId());
        return resourceDataStorageOutputPort.downloadResource(resource, byteRange);
    }

    @Override
    public Collection<Long> deleteResourceById(Collection<Long> ids) {
        var existingResources = resourcePersistenceOutputPort.getAllResourceById(ids);
        var existingResourcesIds = existingResources.stream().map(Resource::getId).toList();

        resourceDataStorageOutputPort.deleteResources(existingResources);
        resourcePersistenceOutputPort.deleteResourceById(existingResourcesIds);

        return existingResourcesIds;
    }

    private Resource buildResource(long contentLength, String contentType) {
        return Resource.builder()
                .name(buildName())
                .path(buildPath())
                .contentType(new ContentType(contentType))
                .contentLength(new ContentLength(contentLength))
                .createdAt(Instant.now())
                .build();
    }

    private ResourceName buildName() {
        return ResourceName.newInstance();
    }

    private String buildPath() {
        return new SimpleDateFormat("yyyy-MM").format(System.currentTimeMillis());
    }
}
