package dev.amir.resourceservice.ct.resource

import com.amazonaws.services.s3.model.PutObjectResult
import dev.amir.resourceservice.ct.BaseCT
import dev.amir.resourceservice.framework.output.rest.response.GetStorageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import java.nio.file.Files
import java.nio.file.Paths

class ResourceControllerCT extends BaseCT {
    private static final String BASE_FILES_PATH = "src/test/resources/files/"

    @Autowired
    private MockMvc mockMvc

    def "Create a Resource from a valid content type should return HTTP Status OK"() {
        given:
        def fileName = "Nature.mp3"
        byte[] resourceData = Files.readAllBytes(Paths.get(BASE_FILES_PATH, fileName))
        def putResult = new PutObjectResult()
        putResult.ETag = "ETag"

        and:
        def responseEntity = ResponseEntity.ok().body([
                new GetStorageResponse(1L, "STAGING", "bucket-name", "custom/path/resource"),
                new GetStorageResponse(2L, "PERMANENT", "bucket-name", "custom/path/resource")
        ])
        1 * restTemplate.exchange(_ as String, _ as HttpMethod, null, _ as ParameterizedTypeReference) >> responseEntity

        expect:
        mockMvc.perform(MockMvcRequestBuilders.post("/resources").content(resourceData))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.id').isNumber())
    }
}
