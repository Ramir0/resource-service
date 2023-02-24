package dev.amir.resourceservice.domain.exception

import spock.lang.Specification

class InvalidResourceExceptionSpec extends Specification {
    def "should throw InvalidResourceException with correct message"() {
        given:
        def expectedMessage = "Invalid resource"

        when:
        def exception = new InvalidResourceException(expectedMessage)

        then:
        exception.message == expectedMessage
    }
}
