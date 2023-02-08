package dev.amir.resourceservice.application.port.input

import dev.amir.resourceservice.application.manager.ResourceMetaDataManager
import dev.amir.resourceservice.application.port.output.ResourceDataStorageOutputPort
import dev.amir.resourceservice.application.port.output.ResourcePersistenceOutputPort
import dev.amir.resourceservice.application.validator.ContentTypeValidator
import dev.amir.resourceservice.domain.entity.Resource
import spock.lang.Specification

class ResourceManagementInputPortSpec extends Specification {
    private final ResourceMetaDataManager resourceMetaDataManager = Mock()
    private final ResourceDataStorageOutputPort resourceDataStorageOutputPort = Mock()
    private final ResourcePersistenceOutputPort resourcePersistenceOutputPort = Mock()
    private final ContentTypeValidator contentTypeValidator = Mock()

    private final ResourceManagementInputPort resourceManagementInputPort = new ResourceManagementInputPort(resourceMetaDataManager, contentTypeValidator, resourceDataStorageOutputPort, resourcePersistenceOutputPort)

    def "createResource should return a Resource with correct data when content type is valid"() {
        Resource resource = null
        given:
        byte[] resourceData = [1, 2, 3, 4, 5]
        String contentType = "content-type"

        when:
        resourceManagementInputPort.createResource(resourceData)

        then:
        1 * resourceMetaDataManager.getContentType(resourceData) >> contentType
        1 * contentTypeValidator.isContentTypeValid(contentType) >> true
        1 * resourceDataStorageOutputPort.uploadResource(_ as Resource, resourceData)
        1 * resourcePersistenceOutputPort.saveResource(_ as Resource) >> { arguments -> resource = arguments.get(0) }

        and:
        !resource.id
        resource.name
        resource.path
        resource.contentType
        !resource.contentLength
        resource.createdAt
    }
}
