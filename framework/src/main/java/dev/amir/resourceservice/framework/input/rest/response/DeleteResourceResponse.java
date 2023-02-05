package dev.amir.resourceservice.framework.input.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteResourceResponse {
    private Iterable<Long> ids;
}
