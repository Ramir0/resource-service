package dev.amir.resourceservice.application.manager;

import dev.amir.resourceservice.domain.entity.Resource;

public interface ResourceMessageManager {
    void sendProcessResourceMessage(Resource resource);
}
