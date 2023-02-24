package dev.amir.resourceservice.domain.exception

import spock.lang.Specification

class ResourceNotFoundExceptionSpec extends Specification {
    def "should throw ResourceNotFoundException with correct message"() {
        given:
        def resourceId = 100L

        when:
        def exception = new ResourceNotFoundException(resourceId)

        then:
        exception.message.contains(resourceId.toString())
    }
}
