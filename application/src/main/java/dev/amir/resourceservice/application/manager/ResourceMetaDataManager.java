package dev.amir.resourceservice.application.manager;

public interface ResourceMetaDataManager {
    String getContentType(byte[] resourceData);
    long getContentLength(byte[] resourceData);
}
