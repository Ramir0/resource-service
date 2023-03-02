package dev.amir.resourceservice.it.resource

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectResult
import dev.amir.resourceservice.domain.exception.InvalidResourceException
import dev.amir.resourceservice.framework.input.rest.request.CreateResourceRequest
import dev.amir.resourceservice.framework.input.rest.service.ResourceService
import dev.amir.resourceservice.framework.output.rabbitmq.message.ProcessResourceMessage
import dev.amir.resourceservice.framework.output.sql.entity.ResourceJpaEntity
import dev.amir.resourceservice.it.BaseIT
import org.springframework.beans.factory.annotation.Autowired

import java.nio.file.Files
import java.nio.file.Paths

class ResourceServiceIT extends BaseIT {
    private static final String BASE_FILES_PATH = "src/test/resources/files/"

    @Autowired
    private ResourceService resourceService

    def "Create a Resource from a valid content type should return HTTP StatusCode OK"() {
        given:
        def fileName = "Nature.mp3"
        byte[] resourceData = Files.readAllBytes(Paths.get(BASE_FILES_PATH, fileName))
        def createResourceRequest = new CreateResourceRequest(resourceData)

        and:
        def putResult = new PutObjectResult()
        putResult.ETag = "ETag"
        def savedResource = new ResourceJpaEntity()
        savedResource.id = 1L
        savedResource.name = UUID.randomUUID().toString()
        savedResource.path = "/"

        when:
        def response = resourceService.createResource(createResourceRequest)

        then:
        1 * s3Client.putObject(_ as String, _ as String, _ as InputStream, _ as ObjectMetadata) >> putResult
        1 * resourceRepository.save(_ as ResourceJpaEntity) >> savedResource
        1 * rabbitTemplate.convertAndSend(_ as String, _ as ProcessResourceMessage)

        and:
        response.statusCode.value() == 200
        response.body.id > 0
    }

    def "Create a Resource from an invalid content type should throw an exception"() {
        given:
        def fileName = "LoremIpsum.txt"
        byte[] resourceData = Files.readAllBytes(Paths.get(BASE_FILES_PATH, fileName))
        def createResourceRequest = new CreateResourceRequest(resourceData)

        when:
        resourceService.createResource(createResourceRequest)

        then:
        0 * s3Client.putObject(_ as String, _ as String, _ as InputStream, _ as ObjectMetadata)
        0 * rabbitTemplate.convertAndSend(_ as String, _ as ProcessResourceMessage)

        and:
        def exception = thrown(InvalidResourceException)
        exception.message == "Invalid content type"
    }
}