package dev.amir.resourceservice.framework.input.rest.service;

import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.framework.input.rest.request.CompleteResourceRequest;
import dev.amir.resourceservice.framework.input.rest.request.CreateResourceRequest;
import dev.amir.resourceservice.framework.input.rest.request.DeleteResourceRequest;
import dev.amir.resourceservice.framework.input.rest.response.CompleteResourceResponse;
import dev.amir.resourceservice.framework.input.rest.response.CreateResourceResponse;
import dev.amir.resourceservice.framework.input.rest.response.DeleteResourceResponse;
import org.springframework.http.ResponseEntity;

public interface ResourceService {
    ResponseEntity<CreateResourceResponse> createResource(CreateResourceRequest request);

    Resource getResourceById(Long id);

    ResponseEntity<DeleteResourceResponse> deleteResource(DeleteResourceRequest request);

    ResponseEntity<CompleteResourceResponse> completeResource(CompleteResourceRequest request);
}
