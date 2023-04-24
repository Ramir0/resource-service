package dev.amir.resourceservice.it

import com.amazonaws.services.s3.AmazonS3
import dev.amir.resourceservice.App
import dev.amir.resourceservice.framework.output.sql.repository.ResourceRepository
import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration])
@SpringBootTest(classes = [App])
class BaseIT extends Specification {
    @SpringBean
    protected AmazonS3 s3Client = Mock(AmazonS3)
    @SpringBean
    protected RabbitTemplate rabbitTemplate = Mock(RabbitTemplate)
    @SpringBean
    protected ResourceRepository resourceRepository = Mock(ResourceRepository)
    @SpringBean
    protected RestTemplate restTemplate = Mock(RestTemplate)
}
