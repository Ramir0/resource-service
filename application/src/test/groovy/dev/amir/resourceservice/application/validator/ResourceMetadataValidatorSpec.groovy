package dev.amir.resourceservice.application.validator

import org.jeasy.random.EasyRandom
import spock.lang.Specification

class ResourceMetadataValidatorSpec extends Specification {
    private final EasyRandom random = new EasyRandom()
    private final ResourceMetadataValidatorImpl validator = new ResourceMetadataValidatorImpl()

    def "isContentTypeInvalid should return true for invalid Content-Type"() {
        expect:
        validator.isContentTypeInvalid(contentType) == expected

        where:
        contentType                | expected
        "text/plain"               | true
        "application/json"         | true
        "application/pdf"          | true
        "application/octet-stream" | true
        "application/msword"       | true
        "application/vnd.ms-excel" | true
        "audio/wav"                | true
        "audio/mp4"                | true
        "audio/mp"                 | true
        ""                         | true
        null                       | true
    }

    def "isContentTypeInvalid should return false for valid Content-Type"() {
        expect:
        validator.isContentTypeInvalid(contentType) == expected

        where:
        contentType  || expected
        "audio/mpeg" || false
        "AUDIO/MPEG" || false
    }

    def "isContentLengthInvalid should return true for invalid Content-Length"() {
        given:
        long minValue = Long.MIN_VALUE
        long maxValue = ResourceMetadataValidatorImpl.MIN_CONTENT_LENGTH
        long randomValue = random.nextLong(minValue, maxValue)

        expect:
        validator.isContentLengthInvalid(minValue)
        validator.isContentLengthInvalid(maxValue)
        validator.isContentLengthInvalid(randomValue)
    }

    def "isContentLengthInvalid should return false for valid Content-Length"() {
        given:
        long minValue = ResourceMetadataValidatorImpl.MIN_CONTENT_LENGTH + 1
        long maxValue = Long.MAX_VALUE
        long randomValue = random.nextLong(minValue, maxValue)

        expect:
        !validator.isContentLengthInvalid(minValue)
        !validator.isContentLengthInvalid(maxValue)
        !validator.isContentLengthInvalid(randomValue)
    }
}
