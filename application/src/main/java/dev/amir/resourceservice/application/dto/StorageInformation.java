package dev.amir.resourceservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageInformation {
    Long id;
    String storageType;
    String bucket;
    String path;
}
