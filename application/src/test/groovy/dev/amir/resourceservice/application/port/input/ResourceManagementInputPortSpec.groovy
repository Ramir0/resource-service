package dev.amir.resourceservice.application.port.input


import dev.amir.resourceservice.application.manager.ResourceMetaDataManager
import dev.amir.resourceservice.application.port.output.ResourceDataStorageOutputPort
import dev.amir.resourceservice.application.port.output.ResourceMessageProducerOutputPort
import dev.amir.resourceservice.application.port.output.ResourcePersistenceOutputPort
import dev.amir.resourceservice.application.retry.RetryAction
import dev.amir.resourceservice.application.retry.RetryExecutor
import dev.amir.resourceservice.application.validator.ResourceMetadataValidator
import dev.amir.resourceservice.domain.entity.Resource
import dev.amir.resourceservice.domain.exception.InvalidResourceException
import spock.lang.Specification

import java.time.Instant

class ResourceManagementInputPortSpec extends Specification {
    private final ResourceMetaDataManager resourceMetaDataManager = Mock()
    private final ResourceDataStorageOutputPort resourceDataStorageOutputPort = Mock()
    private final ResourcePersistenceOutputPort resourcePersistenceOutputPort = Mock()
    private final ResourceMetadataValidator resourceMetadataValidator = Mock()
    private final ResourceMessageProducerOutputPort resourceMessageProducerOutputPort = Mock()
    private final RetryExecutor retryExecutor = Mock()

    private final ResourceManagementInputPort resourceManagementInputPort = new ResourceManagementInputPort(
            resourceMetaDataManager,
            resourceMetadataValidator,
            resourceDataStorageOutputPort,
            resourcePersistenceOutputPort,
            resourceMessageProducerOutputPort,
            retryExecutor
    )

    def "createResource should return a Resource with correct data when content is valid"() {
        Resource resource = null

        given:
        byte[] resourceData = [1, 2, 3, 4, 5]
        String contentType = "content/type"
        long contentLength = 512L

        when:
        resourceManagementInputPort.createResource(resourceData)

        then:
        1 * resourceMetaDataManager.getContentType(resourceData) >> contentType
        1 * resourceMetadataValidator.isContentTypeInvalid(contentType) >> false
        1 * resourceMetaDataManager.getContentLength(resourceData) >> contentLength
        1 * resourceMetadataValidator.isContentLengthInvalid(contentLength) >> false
        1 * resourceDataStorageOutputPort.uploadResource(_ as Resource, resourceData)
        1 * resourcePersistenceOutputPort.saveResource(_ as Resource) >> { args -> resource = args.get(0) }
        1 * retryExecutor.execute(_ as RetryAction)

        and:
        !resource.id
        resource.name
        resource.path
        resource.contentType.value == contentType
        resource.contentLength.value == contentLength
        (resource.createdAt <=> Instant.now()) < 1
    }

    def "createResource should throw InvalidResourceException when content type is invalid"() {
        given:
        byte[] resourceData = [1, 2, 3, 4, 5]
        String contentType = "content-type"

        when:
        resourceManagementInputPort.createResource(resourceData)

        then:
        1 * resourceMetaDataManager.getContentType(resourceData) >> contentType
        1 * resourceMetadataValidator.isContentTypeInvalid(contentType) >> true

        and:
        thrown(InvalidResourceException)
    }

    def "createResource should throw InvalidResourceException when content length is invalid"() {
        given:
        byte[] resourceData = [1, 2, 3, 4, 5]
        String contentType = "content-type"
        Long contentLength = 512L

        when:
        resourceManagementInputPort.createResource(resourceData)

        then:
        1 * resourceMetaDataManager.getContentType(resourceData) >> contentType
        1 * resourceMetadataValidator.isContentTypeInvalid(contentType) >> false
        1 * resourceMetaDataManager.getContentLength(resourceData) >> contentLength
        1 * resourceMetadataValidator.isContentLengthInvalid(contentLength) >> true

        and:
        thrown(InvalidResourceException)
    }
}
