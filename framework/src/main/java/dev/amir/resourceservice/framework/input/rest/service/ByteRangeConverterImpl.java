package dev.amir.resourceservice.framework.input.rest.service;

import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.exception.InvalidResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ByteRangeConverterImpl implements ByteRangeConverter {
    private static final Pattern RANGE_HEADER_PATTERN = Pattern.compile("^bytes=(\\d*)-(\\d*)$");

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
        if (StringUtils.hasText(matcher.group(1))) {
            byteRange.setStart(Long.valueOf(matcher.group(1)));
        }
        if (StringUtils.hasText(matcher.group(2))) {
            byteRange.setEnd(Long.valueOf(matcher.group(2)));
        }
        if (byteRange.getEnd() == null && byteRange.getStart() == null) {
            throw new InvalidResourceException("Range Header must have at least one value");
        }
        return Optional.of(byteRange);
    }
}
