package dev.amir.resourceservice.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ByteRange {
    private Long start;
    private Long end;
}
