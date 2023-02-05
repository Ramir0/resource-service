package dev.amir.resourceservice.framework.input.rest;

import dev.amir.resourceservice.framework.input.rest.request.DeleteResourceRequest;
import dev.amir.resourceservice.framework.input.rest.response.CreateResourceResponse;
import dev.amir.resourceservice.framework.input.rest.response.DeleteResourceResponse;
import dev.amir.resourceservice.framework.input.rest.service.ByteRangeConverter;
import dev.amir.resourceservice.framework.input.rest.service.ResourceService;
import dev.amir.resourceservice.framework.input.rest.service.ResourceStreamReaderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("resources")
public class ResourcesController {
    private final ResourceService resourceService;
    private final ResourceStreamReaderService resourceStreamReaderService;
    private final ByteRangeConverter byteRangeConverter;

    @PostMapping
    public ResponseEntity<CreateResourceResponse> createResource(@RequestBody byte[] resource) {
        return resourceService.createResource(resource);
    }

    @GetMapping("{id}")
    public ResponseEntity<StreamingResponseBody> getResource(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        var byteRange = byteRangeConverter.convert(rangeHeader);

        if (byteRange.isPresent()) {
            return resourceStreamReaderService.getPartialResponse(id, byteRange.get());
        }
        return resourceStreamReaderService.getResponse(id);
    }

    @DeleteMapping()
    public ResponseEntity<DeleteResourceResponse> deleteResource(
            @Valid @RequestParam("id") DeleteResourceRequest request) {
        return resourceService.deleteResource(request);
    }
}
