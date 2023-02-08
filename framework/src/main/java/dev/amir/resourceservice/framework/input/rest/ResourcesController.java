package dev.amir.resourceservice.framework.input.rest;

import dev.amir.resourceservice.framework.input.rest.request.CreateResourceRequest;
import dev.amir.resourceservice.framework.input.rest.request.DeleteResourceRequest;
import dev.amir.resourceservice.framework.input.rest.request.GetResourceRequest;
import dev.amir.resourceservice.framework.input.rest.response.CreateResourceResponse;
import dev.amir.resourceservice.framework.input.rest.response.DeleteResourceResponse;
import dev.amir.resourceservice.framework.input.rest.response.GetResourceResponse;
import dev.amir.resourceservice.framework.input.rest.service.ResourceService;
import dev.amir.resourceservice.framework.input.rest.service.ResourceStreamReaderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("resources")
public class ResourcesController {
    private final ResourceService resourceService;
    private final ResourceStreamReaderService resourceStreamReaderService;

    @PostMapping
    public ResponseEntity<CreateResourceResponse> createResource(
            @RequestBody byte[] requestBody) {
        var request = new CreateResourceRequest(requestBody);
        return resourceService.createResource(request);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetResourceResponse> getResource(
            @PathVariable("id") Long resourceId,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        var request = new GetResourceRequest(resourceId, rangeHeader);
        return resourceStreamReaderService.getResource(request);
    }

    @DeleteMapping()
    public ResponseEntity<DeleteResourceResponse> deleteResource(
            @Valid @RequestParam("id") DeleteResourceRequest request) {
        return resourceService.deleteResource(request);
    }
}
