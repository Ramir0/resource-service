package dev.amir.resourceservice.framework.output.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetStorageResponse {
    private Long id;
    private String storageType;
    private String bucket;
    private String path;
}
