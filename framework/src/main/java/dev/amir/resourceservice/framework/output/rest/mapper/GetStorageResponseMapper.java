package dev.amir.resourceservice.framework.output.rest.mapper;

import dev.amir.resourceservice.application.dto.StorageInformation;
import dev.amir.resourceservice.framework.output.rest.response.GetStorageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GetStorageResponseMapper {
    StorageInformation convert(GetStorageResponse response);

    default Collection<StorageInformation> convertCollection(Collection<GetStorageResponse> responses) {
        return responses.stream().map(this::convert).toList();
    }
}
