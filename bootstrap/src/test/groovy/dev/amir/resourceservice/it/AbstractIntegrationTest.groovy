package dev.amir.resourceservice.it

import com.amazonaws.services.s3.AmazonS3
import dev.amir.resourceservice.App
import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@AutoConfigureMockMvc
@SpringBootTest(classes = [App], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbstractIntegrationTest extends Specification {
    @SpringBean
    protected AmazonS3 s3Client = Mock(AmazonS3)
    @SpringBean
    protected RabbitTemplate rabbitTemplate = Mock(RabbitTemplate)
}
