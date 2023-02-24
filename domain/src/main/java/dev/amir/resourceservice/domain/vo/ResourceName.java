package dev.amir.resourceservice.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceName {
    private final UUID value;

    public static ResourceName getInstance(String name) {
        return new ResourceName(UUID.fromString(name));
    }

    public static ResourceName newInstance() {
        return new ResourceName(UUID.randomUUID());
    }

    public String getValue() {
        return toString();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
