package dev.amir.resourceservice.domain.vo

import spock.lang.Specification

class ResourceNameSpec extends Specification {
    def "creating a new instance of ResourceName should generate a random UUID"() {
        given:
        def resourceName = ResourceName.newInstance()

        expect:
        resourceName.value != null
    }

    def "creating a new instance of ResourceName with a string name should generate the expected UUID"() {
        given:
        def name = "00000000-0000-0000-0000-000000000001"
        def resourceName = ResourceName.getInstance(name)

        expect:
        resourceName.value == name
    }

    def "creating a new instance of ResourceName with an invalid string name should throw an IllegalArgumentException"() {
        given:
        def invalidName = "invalid-uuid-string"

        when:
        ResourceName.getInstance(invalidName)

        then:
        thrown(IllegalArgumentException)
    }
}
