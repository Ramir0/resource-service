package dev.amir.resourceservice.application.validator;

import org.springframework.stereotype.Service;

@Service
public class ResourceMetadataValidatorImpl implements ResourceMetadataValidator {
    private final static String VALID_CONTENT_TYPE = "audio/mpeg";
    protected final static long MIN_CONTENT_LENGTH = 128L;

    @Override
    public boolean isContentTypeInvalid(String contentType) {
        return !VALID_CONTENT_TYPE.equalsIgnoreCase(contentType);
    }

    @Override
    public boolean isContentLengthInvalid(long contentLength) {
        return contentLength <= MIN_CONTENT_LENGTH;
    }
}
