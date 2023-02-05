package dev.amir.resourceservice.application.manager;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

@Service
public class ResourceMetaDataManagerImpl implements ResourceMetaDataManager {
    private final Tika tika = new Tika();

    @Override
    public String getContentType(byte[] resourceData) {
        return tika.detect(resourceData);
    }

    @Override
    public long getContentLength(byte[] resourceData) {
        return resourceData.length;
    }
}
