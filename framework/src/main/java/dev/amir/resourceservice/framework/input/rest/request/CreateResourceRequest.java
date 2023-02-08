package dev.amir.resourceservice.framework.input.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateResourceRequest {
    private byte[] resourceData;
}
