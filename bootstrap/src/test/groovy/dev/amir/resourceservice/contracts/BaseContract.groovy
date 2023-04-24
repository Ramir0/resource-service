package dev.amir.resourceservice.contracts

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectResult
import dev.amir.resourceservice.App
import dev.amir.resourceservice.framework.output.rabbitmq.message.ProcessResourceMessage
import dev.amir.resourceservice.framework.output.rest.response.GetStorageResponse
import dev.amir.resourceservice.framework.output.sql.entity.ResourceJpaEntity
import dev.amir.resourceservice.framework.output.sql.repository.ResourceRepository
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration])
@SpringBootTest(classes = [App])
abstract class BaseContract extends Specification {

    @Autowired
    WebApplicationContext webApplicationContext

    @SpringBean
    protected AmazonS3 s3Client = Mock(AmazonS3)
    @SpringBean
    protected RabbitTemplate rabbitTemplate = Mock(RabbitTemplate)
    @SpringBean
    protected ResourceRepository resourceRepository = Mock(ResourceRepository)
    @SpringBean
    protected RestTemplate restTemplate = Mock(RestTemplate)

    def setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext)
        initializeMocks()
    }

    private void initializeMocks() {
        // Retrieve Storage Information
        def responseEntity = ResponseEntity.ok().body([
                new GetStorageResponse(1L, "STAGING", "bucket-name", "custom/path/resource"),
                new GetStorageResponse(2L, "PERMANENT", "bucket-name", "custom/path/resource")
        ])
        restTemplate.exchange(_ as String, _ as HttpMethod, null, _ as ParameterizedTypeReference) >> responseEntity

        // Upload file to Amazon S3
        def putResult = new PutObjectResult()
        putResult.ETag = "ETag"
        s3Client.putObject(_ as String, _ as String, _ as InputStream, _ as ObjectMetadata) >> putResult

        // Persist data
        def savedResource = new ResourceJpaEntity()
        savedResource.id = 1L
        savedResource.name = UUID.randomUUID().toString()
        savedResource.path = "/"
        resourceRepository.save(_ as ResourceJpaEntity) >> savedResource

        // Send message
        def message = new ProcessResourceMessage(1L)
        rabbitTemplate.convertAndSend(_ as String, message)
    }
}
