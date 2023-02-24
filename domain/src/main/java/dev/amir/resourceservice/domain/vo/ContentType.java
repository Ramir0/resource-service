package dev.amir.resourceservice.domain.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ContentType {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+/[a-zA-Z0-9+]+$")
    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
