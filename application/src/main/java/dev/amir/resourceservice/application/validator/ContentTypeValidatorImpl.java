package dev.amir.resourceservice.application.validator;

import org.springframework.stereotype.Service;

@Service
public class ContentTypeValidatorImpl implements ContentTypeValidator {
    private final static String VALID_CONTENT_TYPE = "audio/mpeg";

    @Override
    public boolean isContentTypeValid(String contentType) {
        return VALID_CONTENT_TYPE.equalsIgnoreCase(contentType);
    }
}
