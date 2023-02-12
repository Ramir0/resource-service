package dev.amir.resourceservice.application.validator;

import org.springframework.stereotype.Service;

@Service
public class ResourceMetadataValidatorImpl implements ResourceMetadataValidator {
    private final static String VALID_CONTENT_TYPE = "audio/mpeg";

    @Override
    public boolean isContentTypeInvalid(String contentType) {
        return !VALID_CONTENT_TYPE.equals(contentType);
    }

    @Override
    public boolean isContentLengthInvalid(long contentLength) {
        return contentLength > 0;
    }
}
