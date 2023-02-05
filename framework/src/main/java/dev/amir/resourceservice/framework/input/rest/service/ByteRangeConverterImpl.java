package dev.amir.resourceservice.framework.input.rest.service;

import dev.amir.resourceservice.domain.entity.ByteRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ByteRangeConverterImpl implements ByteRangeConverter {
    private static final Pattern RANGE_HEADER_PATTERN = Pattern.compile("^bytes=(\\d+)-(\\d+)$");

    @Override
    public Optional<ByteRange> convert(String rangeHeader) {
        if (!StringUtils.hasText(rangeHeader)) {
            return Optional.empty();
        }
        Matcher matcher = RANGE_HEADER_PATTERN.matcher(rangeHeader);
        if (!matcher.matches()) {
            log.warn("Range Header is invalid: {}", rangeHeader);
            return Optional.empty();
        }

        var byteRange = new ByteRange();
        byteRange.setStartByte(Long.valueOf(matcher.group(1)));
        byteRange.setEndByte(Long.valueOf(matcher.group(2)));
        return Optional.of(byteRange);
    }
}
