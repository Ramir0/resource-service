package dev.amir.resourceservice.framework.output.rest.client;

import dev.amir.resourceservice.application.exception.UnexpectedStorageInformationException;
import dev.amir.resourceservice.framework.output.rest.response.GetStorageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageServiceRestClientImpl implements StorageServiceRestClient {
    private static final String STORAGES = "storages";
    private final RestTemplate restTemplate;

    @Value("${services.storage-service.url}")
    private String storageServiceUrl;

    @Override
    public Collection<GetStorageResponse> getAllStorages() {
        var getStoragesUrl = String.format("%s/%s", storageServiceUrl, STORAGES);
        var response = restTemplate.exchange(
                getStoragesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GetStorageResponse>>() {
                }
        );
        if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            throw new UnexpectedStorageInformationException(String.format(
                    "An error occurred getting Storage Information. ResponseCode: [%s]",
                    response.getStatusCode()
            ));
        }

        log.debug("Retrieved: [{}]", response.getBody());
        return response.getBody();
    }
}
