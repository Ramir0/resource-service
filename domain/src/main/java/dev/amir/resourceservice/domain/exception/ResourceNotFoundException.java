package dev.amir.resourceservice.domain.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long resourceId) {
        super(String.format("Resource with id: %d not found", resourceId));
    }
}
