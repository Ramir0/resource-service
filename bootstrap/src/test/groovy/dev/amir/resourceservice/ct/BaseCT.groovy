package dev.amir.resourceservice.ct

import dev.amir.resourceservice.App
import dev.amir.resourceservice.domain.profile.Profiles
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@ActiveProfiles(Profiles.INTEGRATION)
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(classes = [App], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseCT extends Specification {

    // Amazon S3 env
    private static final Integer[] S3_PORTS = [4566, 4571, 4572]
    private static final String S3_ACCESS_KEY = "local-test-access-key"
    private static final String S3_SECRET_KEY = "local-test-secret-key"
    private static final String S3_REGION = "us-east-1"

    // RabbitMQ env
    private static final Integer[] RABBITMQ_PORTS = [5672, 15672]

    // Test Containers
    @Shared
    private static GenericContainer rabbitMQContainer = new GenericContainer<>("rabbitmq:3.11")
            .withExposedPorts(RABBITMQ_PORTS)
            .waitingFor(Wait.forLogMessage(".*Server startup complete.*", 1))

    @Shared
    private static GenericContainer s3Container = new GenericContainer<>("localstack/localstack:1.4")
            .withExposedPorts(S3_PORTS)
            .withEnv("SERVICES", "s3")
            .withEnv("EAGER_SERVICE_LOADING", "1")
            .withEnv("PERSISTENCE", "1")
            .withEnv("ACCESS_KEY", S3_ACCESS_KEY)
            .withEnv("SECRET_KEY", S3_SECRET_KEY)
            .withEnv("AWS_REGION", S3_REGION)
            .waitingFor(Wait.forLogMessage(".*Ready\\.\\n", 1))

    @DynamicPropertySource
    private static void configure(DynamicPropertyRegistry registry) {
        // Start Test Containers
        rabbitMQContainer.start()
        s3Container.start()

        // RabbitMQ props
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost)
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getFirstMappedPort)

        // Amazon S3 props
        registry.add("services.aws.s3.url", () -> "http://" + s3Container.getHost() + ":" + s3Container.getFirstMappedPort())
        registry.add("cloud.aws.region.static", () -> S3_REGION)
        registry.add("cloud.aws.credentials.accessKey", () -> S3_ACCESS_KEY)
        registry.add("cloud.aws.credentials.secretKey", () -> S3_SECRET_KEY)
    }
}
