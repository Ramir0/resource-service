package dev.amir.resourceservice.framework.input.rest.service;

import dev.amir.resourceservice.application.usecase.ResourceManagementUseCase;
import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.framework.input.rest.request.GetResourceRequest;
import dev.amir.resourceservice.framework.input.rest.response.GetResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceStreamReaderServiceImpl implements ResourceStreamReaderService {
    private final static String CONTENT_RANGE_FORMAT = "bytes %d-%d/%d";

    private final ResourceManagementUseCase resourceManagementUseCase;
    private final ByteRangeConverter byteRangeConverter;
    private final ResourceService resourceService;

    @Override
    public ResponseEntity<GetResourceResponse> getResource(GetResourceRequest request) {
        return byteRangeConverter.convert(request.getRangeHeader())
                .map(range -> getPartialResponse(request.getId(), range))
                .orElseGet(() -> getResponse(request.getId()));
    }

    private ResponseEntity<GetResourceResponse> getResponse(Long resourceId) {
        var resource = resourceService.getResourceById(resourceId);
        var response = buildResponseTemplate(resource, HttpStatus.OK);

        return response.body(outputStream -> outputStream.write(resourceManagementUseCase.getResourceData(resource)));
    }

    private ResponseEntity<GetResourceResponse> getPartialResponse(Long resourceId, ByteRange byteRange) {
        var resource = resourceService.getResourceById(resourceId);
        var HttpStatusCode = buildStatusCode(byteRange.getStartByte(), byteRange.getEndByte(), resource.getContentLength());
        var response = buildResponseTemplate(resource, HttpStatusCode);
        response.header(HttpHeaders.CONTENT_RANGE, buildContentRange(resource, byteRange));

        return response.body(outputStream ->
                outputStream.write(
                        resourceManagementUseCase.getPartialResourceData(
                                resource,
                                byteRange.getStartByte(),
                                byteRange.getEndByte()
                        )
                )
        );
    }

    private HttpStatusCode buildStatusCode(Long startByte, Long endByte, Long maxByte) {
        return endByte >= maxByte && startByte == 0L ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT;
    }

    private String buildContentRange(Resource resource, ByteRange byteRange) {
        return String.format(CONTENT_RANGE_FORMAT, byteRange.getStartByte(), byteRange.getEndByte(), resource.getContentLength());
    }

    private ResponseEntity.BodyBuilder buildResponseTemplate(Resource resource, HttpStatusCode statusCode) {
        var contentType = MediaType.parseMediaType("audio/mpeg;charset=UTF-8");
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getName());

        return ResponseEntity.status(statusCode)
                .headers(headers)
                .contentType(contentType);
    }
}
