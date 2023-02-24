package dev.amir.resourceservice.domain.vo

import jakarta.validation.Validation
import jakarta.validation.Validator
import spock.lang.Specification

class ContentLengthSpec extends Specification {
    private final Validator validator = Validation.buildDefaultValidatorFactory().validator

    def "creating a new instance of ContentLength should contain a value"() {
        given:
        def contentLength = new ContentLength(longValue)

        when:
        def violations = validator.validate(contentLength)

        then:
        violations.empty
        contentLength.value == longValue

        where:
        longValue       | _
        129L            | _
        256L            | _
        Long.MAX_VALUE  | _
    }

    def "creating a new instance of ContentLength with invalid data should have Constraints"() {
        given:
        def contentLength = new ContentLength(invalidValue)

        when:
        def violations = validator.validate(contentLength)

        then:
        violations.size() == 1
        violations.every { v -> v.getMessage().startsWith(expectedMessage) }

        where:
        invalidValue      | expectedViolations | expectedMessage
        128L              | 1                  | "must be greater than or equal to"
        -256L             | 1                  | "must be greater than or equal to"
        null              | 1                  | "must not be null"
    }
}
