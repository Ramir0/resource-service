package dev.amir.resourceservice.application.validator;

public interface ResourceMetadataValidator {
    boolean isContentTypeInvalid(String contentType);
    boolean isContentLengthInvalid(long contentLength);
}
