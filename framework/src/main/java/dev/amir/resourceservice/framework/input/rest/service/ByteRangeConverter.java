package dev.amir.resourceservice.framework.input.rest.service;

import dev.amir.resourceservice.domain.entity.ByteRange;

import java.util.Optional;

public interface ByteRangeConverter {
    Optional<ByteRange> convert(String rangeHeader);
}
