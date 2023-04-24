package dev.amir.resourceservice.framework.output.sql.entity;

import dev.amir.resourceservice.domain.vo.ResourceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(name = "resources")
@NoArgsConstructor
@AllArgsConstructor
public class ResourceJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String path;

    @Column
    private String contentType;

    @Column
    private Long contentLength;

    @Column
    private ResourceStatus status;

    @Column
    private Instant createdAt;
}
