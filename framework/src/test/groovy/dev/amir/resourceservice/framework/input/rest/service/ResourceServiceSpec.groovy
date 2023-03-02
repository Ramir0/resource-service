package dev.amir.resourceservice.framework.input.rest.service

import dev.amir.resourceservice.application.usecase.ResourceManagementUseCase
import dev.amir.resourceservice.domain.entity.Resource
import dev.amir.resourceservice.domain.exception.UnexpectedResourceException
import dev.amir.resourceservice.framework.input.rest.request.CreateResourceRequest
import dev.amir.resourceservice.framework.input.rest.request.DeleteResourceRequest
import org.springframework.http.HttpStatus
import spock.lang.Specification

class ResourceServiceSpec extends Specification {
    private final ResourceManagementUseCase resourceManagementUseCase = Mock()
    private final ResourceServiceImpl resourceService = new ResourceServiceImpl(resourceManagementUseCase)

    def "createResource should return a ResponseEntity with code 200"() {
        given:
        byte[] resourceData = [1, 2, 3]
        def request = new CreateResourceRequest(resourceData)

        when:
        def response = resourceService.createResource(request)

        then:
        1 * resourceManagementUseCase.createResource(resourceData) >> Resource.builder().id(1).build()

        and:
        response.statusCode == HttpStatus.OK
        response.hasBody()
    }

    def "createResource should throw an UnexpectedResourceException when creation fails"() {
        given:
        byte[] resourceData = [1, 2, 3]
        def request = new CreateResourceRequest(resourceData)

        when:
        resourceService.createResource(request)

        then:
        1 * resourceManagementUseCase.createResource(resourceData) >> null

        and:
        def exception = thrown(UnexpectedResourceException)
        exception.message == "Resource is null or does not have a valid Id"
    }

    def "getResourceById should return a Resource"() {
        given:
        def resourceId = 100L

        when:
        def response = resourceService.getResourceById(resourceId)

        then:
        1 * resourceManagementUseCase.getResourceById(resourceId) >> Resource.builder().id(resourceId).build()

        and:
        response.id == resourceId
    }

    def "deleteResource should return the Id of deleted Resource"() {
        given:
        def ids = "1"
        def request = new DeleteResourceRequest(ids)

        when:
        def response = resourceService.deleteResource(request)

        then:
        1 * resourceManagementUseCase.deleteResourceById(_) >> [1]

        and:
        response.statusCode == HttpStatus.OK
        response.hasBody()
        response.body.ids.size() == 1
    }

    def "deleteResource should throw UnexpectedResourceException when deletion fails"() {
        given:
        def ids = "1"
        def request = new DeleteResourceRequest(ids)

        when:
        resourceService.deleteResource(request)

        then:
        1 * resourceManagementUseCase.deleteResourceById(_) >> { throw new NullPointerException("test") }

        and:
        def exception = thrown(UnexpectedResourceException)
        exception.message == "java.lang.NullPointerException: test"
    }

    def "deleteResource should throw UnexpectedResourceException when parsing Ids fails"() {
        given:
        def ids = "1"
        def request = new DeleteResourceRequest(ids)

        when:
        resourceService.deleteResource(request)

        then:
        1 * resourceManagementUseCase.deleteResourceById(_) >> { throw new NumberFormatException() }

        and:
        def exception = thrown(UnexpectedResourceException)
        exception.message == "Invalid list of ids: [1]"
    }
}
