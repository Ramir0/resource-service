package dev.amir.resourceservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageInformation {
    Long id;
    String storageType;
    String bucket;
    String path;
}
