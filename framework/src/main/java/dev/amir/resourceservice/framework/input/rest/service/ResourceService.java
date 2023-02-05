package dev.amir.resourceservice.framework.input.rest.service;

import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.framework.input.rest.request.DeleteResourceRequest;
import dev.amir.resourceservice.framework.input.rest.response.CreateResourceResponse;
import dev.amir.resourceservice.framework.input.rest.response.DeleteResourceResponse;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;

public interface ResourceService {
    ResponseEntity<CreateResourceResponse> createResource(byte[] resourceData);

    Resource getResourceById(Long id);

    ResponseEntity<DeleteResourceResponse> deleteResource(DeleteResourceRequest request);
}
