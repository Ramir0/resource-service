package dev.amir.resourceservice.domain.entity;

import dev.amir.resourceservice.domain.vo.ResourceName;
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
    private ResourceName name;
    private String path;
    private String contentType;
    private Long contentLength;
    private Instant createdAt;
}
