package dev.amir.resourceservice.domain.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ContentLength {
    @NotNull
    @Min(129)
    private final Long value;

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
