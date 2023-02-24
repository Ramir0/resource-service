package dev.amir.resourceservice.domain.vo

import jakarta.validation.Validation
import jakarta.validation.Validator
import spock.lang.Specification

class ContentTypeSpec extends Specification {
    private final Validator validator = Validation.buildDefaultValidatorFactory().validator

    def "creating a new instance of ContentType should contain a value"() {
        given:
        def contentType = new ContentType(mimeTypeString)
        when:
        def violations = validator.validate(contentType)

        then:
        violations.empty

        where:
        mimeTypeString     | expectedViolations
        "text/plain"       | true
        "application/json" | true
        "application/pdf"  | true
        "audio/mpeg"       | true
    }

    def "creating a new instance of ContentType with invalid data should have Constraints"() {
        given:
        def contentType = new ContentType(mimeTypeString)

        when:
        def violations = validator.validate(contentType)

        then:
        violations.size() == expectedViolations
        violations.every { v -> v.getMessage().startsWith(expectedMessage) }

        where:
        mimeTypeString    | expectedViolations | expectedMessage
        "text/plain/mpeg" | 1                  | "must match"
        "1234"            | 1                  | "must match"
        "abcd"            | 1                  | "must match"
        ""                | 1                  | "must match"
        null              | 1                  | "must not be null"
    }
}
