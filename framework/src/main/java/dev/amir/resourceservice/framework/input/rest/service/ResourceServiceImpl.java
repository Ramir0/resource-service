package dev.amir.resourceservice.framework.input.rest.service;

import dev.amir.resourceservice.application.usecase.ResourceManagementUseCase;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.exception.UnexpectedResourceException;
import dev.amir.resourceservice.framework.input.rest.request.DeleteResourceRequest;
import dev.amir.resourceservice.framework.input.rest.response.CreateResourceResponse;
import dev.amir.resourceservice.framework.input.rest.response.DeleteResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceManagementUseCase resourceManagementUseCase;

    @Override
    public ResponseEntity<CreateResourceResponse> createResource(byte[] resourceData) {
        var resource = resourceManagementUseCase.createResource(resourceData);
        return ResponseEntity.ok(new CreateResourceResponse(resource.getId()));
    }

    @Override
    public Resource getResourceById(Long id) {
        return resourceManagementUseCase.getResourceById(id);
    }

    @Override
    public ResponseEntity<DeleteResourceResponse> deleteResource(DeleteResourceRequest request) {
        try {
            var ids = getIdsFromRequest(request);
            var deletedResourceIds = resourceManagementUseCase.deleteResourceById(ids);
            return ResponseEntity.ok(new DeleteResourceResponse(deletedResourceIds));
        } catch (NumberFormatException exception) {
            throw new UnexpectedResourceException(String.format("Invalid list of ids: [%s]", request.getIds()), exception);
        } catch (DataAccessException exception) {
            throw new UnexpectedResourceException(exception);
        }
    }

    private Collection<Long> getIdsFromRequest(DeleteResourceRequest request) {
        return Arrays.stream(request.getIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
