package dev.amir.resourceservice.framework.input.rest.service;

import dev.amir.resourceservice.framework.input.rest.request.GetResourceRequest;
import dev.amir.resourceservice.framework.input.rest.response.GetResourceResponse;
import org.springframework.http.ResponseEntity;

public interface ResourceStreamReaderService {
    ResponseEntity<GetResourceResponse> getResource(GetResourceRequest request);
}
