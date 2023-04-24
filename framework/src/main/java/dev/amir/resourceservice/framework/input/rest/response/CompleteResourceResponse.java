package dev.amir.resourceservice.framework.input.rest.response;

import dev.amir.resourceservice.domain.vo.ResourceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteResourceResponse {
    private ResourceStatus status;
}
