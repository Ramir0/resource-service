package dev.amir.resourceservice.framework.input.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteResourceRequest {
    @NotNull(message = "Resource Id is required")
    private Long id;
}
