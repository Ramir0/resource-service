package dev.amir.resourceservice.application.port.input

import dev.amir.resourceservice.application.manager.ResourceMessageManager
import dev.amir.resourceservice.application.manager.ResourceMetaDataManager
import dev.amir.resourceservice.application.port.output.ResourceDataStorageOutputPort
import dev.amir.resourceservice.application.port.output.ResourcePersistenceOutputPort
import dev.amir.resourceservice.application.validator.ResourceMetadataValidator
import dev.amir.resourceservice.domain.entity.Resource
import spock.lang.Specification

import java.text.SimpleDateFormat
import java.time.Instant

class ResourceManagementInputPortSpec extends Specification {
    private final ResourceMetaDataManager resourceMetaDataManager = Mock()
    private final ResourceDataStorageOutputPort resourceDataStorageOutputPort = Mock()
    private final ResourcePersistenceOutputPort resourcePersistenceOutputPort = Mock()
    private final ResourceMessageManager resourceMessageManager = Mock()
    private final ResourceMetadataValidator resourceMetadataValidator = Mock()

    private final ResourceManagementInputPort resourceManagementInputPort = new ResourceManagementInputPort(resourceMetaDataManager, resourceMetadataValidator, resourceDataStorageOutputPort, resourcePersistenceOutputPort, resourceMessageManager)

    def "createResource should return a Resource with correct data when content type is valid"() {
        Resource resource = null
        given:
        byte[] resourceData = [1, 2, 3, 4, 5]
        String contentType = "content-type"
        Long contentLength = 5

        when:
        resourceManagementInputPort.createResource(resourceData)

        then:
        1 * resourceMetaDataManager.getContentType(resourceData) >> contentType
        1 * resourceMetaDataManager.getContentLength(resourceData) >> contentLength
        1 * resourceMetadataValidator.isContentTypeInvalid(contentType) >> false
        1 * resourceMetadataValidator.isContentLengthInvalid(contentLength) >> false
        1 * resourceDataStorageOutputPort.uploadResource(_ as Resource, resourceData)
        1 * resourcePersistenceOutputPort.saveResource(_ as Resource) >> { arguments -> resource = arguments.get(0) }
        1 * resourceMessageManager.sendProcessResourceMessage(_ as Resource)

        and:
        !resource.id
        resource.name
        resource.path == new SimpleDateFormat("yyyy-MM").format(System.currentTimeMillis())
        resource.contentType == contentType
        resource.contentLength == contentLength
        (resource.createdAt <=> Instant.now()) < 1
    }
}
