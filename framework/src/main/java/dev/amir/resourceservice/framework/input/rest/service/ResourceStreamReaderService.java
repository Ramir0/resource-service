package dev.amir.resourceservice.framework.input.rest.service;

import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface ResourceStreamReaderService {
    ResponseEntity<StreamingResponseBody> getResponse(Long resourceId);
    ResponseEntity<StreamingResponseBody> getPartialResponse(Long resourceId, ByteRange byteRange);
}
