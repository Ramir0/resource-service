package dev.amir.resourceservice.application.port.input;

import dev.amir.resourceservice.application.manager.ResourceMetaDataManager;
import dev.amir.resourceservice.application.port.output.ResourceMessageProducerOutputPort;
import dev.amir.resourceservice.application.port.output.ResourcePersistenceOutputPort;
import dev.amir.resourceservice.application.retry.RetryExecutor;
import dev.amir.resourceservice.application.service.ResourceDataStorageService;
import dev.amir.resourceservice.application.usecase.ResourceManagementUseCase;
import dev.amir.resourceservice.application.validator.ResourceMetadataValidator;
import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.exception.InvalidResourceException;
import dev.amir.resourceservice.domain.exception.ResourceNotFoundException;
import dev.amir.resourceservice.domain.vo.ContentLength;
import dev.amir.resourceservice.domain.vo.ContentType;
import dev.amir.resourceservice.domain.vo.ResourceName;
import dev.amir.resourceservice.domain.vo.ResourceStatus;
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
    private final ResourceDataStorageService resourceDataStorageService;
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
        resourceDataStorageService.uploadResource(newResource, resourceData);
        var savedResource = resourcePersistenceOutputPort.saveResource(newResource);
        retryExecutor.execute(() -> resourceMessageProducerOutputPort.sendProcessResourceMessage(savedResource));

        log.info("Resource with Id: [{}] was successfully created with ResourceStatus: {}", savedResource.getId(), savedResource.getStatus());
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
        return resourceDataStorageService.downloadResource(resource);
    }

    @Override
    public byte[] getPartialResourceData(Resource resource, ByteRange byteRange) {
        log.info("Get partial [{}] ResourceData of Resource with Id [{}]", byteRange, resource.getId());
        return resourceDataStorageService.downloadResource(resource, byteRange);
    }

    @Override
    public Collection<Long> deleteResourceById(Collection<Long> ids) {
        var existingResources = resourcePersistenceOutputPort.getAllResourceById(ids);
        var existingResourcesIds = existingResources.stream().map(Resource::getId).toList();

        resourceDataStorageService.deleteResources(existingResources);
        resourcePersistenceOutputPort.deleteResourceById(existingResourcesIds);

        return existingResourcesIds;
    }

    @Override
    public Resource completeResourceById(Long resourceId) {
        var resource = resourcePersistenceOutputPort.getResourceById(resourceId)
                .filter(r -> r.getStatus() == ResourceStatus.STAGING)
                .orElseThrow(
                        () -> new InvalidResourceException("No resource found with id: " + resourceId + " or resource is not processing")
                );
        resourceDataStorageService.moveResource(resource);
        resource.setStatus(ResourceStatus.PERMANENT);
        resourcePersistenceOutputPort.saveResource(resource);

        return resource;
    }

    private Resource buildResource(long contentLength, String contentType) {
        return Resource.builder()
                .name(buildName())
                .path(buildPath())
                .contentType(new ContentType(contentType))
                .contentLength(new ContentLength(contentLength))
                .status(ResourceStatus.STAGING)
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
