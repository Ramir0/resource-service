package dev.amir.resourceservice.it.resource

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectResult
import dev.amir.resourceservice.framework.output.rabbitmq.message.ProcessResourceMessage
import dev.amir.resourceservice.it.AbstractIntegrationTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import java.nio.file.Files
import java.nio.file.Paths

class ResourceControllerIT extends AbstractIntegrationTest {
    private static final String BASE_FILES_PATH = "src/test/resources/files/"

    @Autowired
    private MockMvc mockMvc

    def "Create a Resource from a valid content type should return HTTP Code 200"() {
        given:
        def fileName = "Nature.mp3"
        byte[] resourceData = Files.readAllBytes(Paths.get(BASE_FILES_PATH, fileName))
        def putResult = new PutObjectResult()
        putResult.ETag = "ETag"

        when:
        mockMvc.perform(MockMvcRequestBuilders.post("/resources").content(resourceData))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.id').isNumber())

        then:
        1 * s3Client.putObject(_ as String, _ as String, _ as InputStream, _ as ObjectMetadata) >> putResult
        1 * rabbitTemplate.convertAndSend(_ as String, _ as ProcessResourceMessage)
    }

    def "Create a Resource from an invalid content type should return HTTP Code 404"() {
        given:
        def fileName = "LoremIpsum.txt"
        byte[] resourceData = Files.readAllBytes(Paths.get(BASE_FILES_PATH, fileName))

        when:
        mockMvc.perform(MockMvcRequestBuilders.post("/resources").content(resourceData))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Validation failed or request body is invalid MP3"))

        then:
        0 * s3Client.putObject(_ as String, _ as String, _ as InputStream, _ as ObjectMetadata)
        0 * rabbitTemplate.convertAndSend(_ as String, _ as ProcessResourceMessage)
    }
}