package dev.amir.resourceservice.application.exception;

public class StorageInformationNotFoundException extends RuntimeException {
    public StorageInformationNotFoundException(String storageType) {
        super("No storage information found for storage type: " + storageType);
    }
}
