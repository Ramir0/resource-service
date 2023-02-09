package dev.amir.resourceservice.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
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

    @Override
    public String toString() {
        return value.toString();
    }
}
