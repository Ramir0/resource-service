package dev.amir.resourceservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private Long id;
    private String name;
    private String path;
    private String contentType;
    private Long contentLength;
    private Instant createdAt;
}
