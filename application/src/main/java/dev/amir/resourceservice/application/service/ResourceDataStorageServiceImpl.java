package dev.amir.resourceservice.application.service;

import dev.amir.resourceservice.application.dto.StorageInformation;
import dev.amir.resourceservice.application.exception.StorageInformationNotFoundException;
import dev.amir.resourceservice.application.port.output.ResourceDataStorageOutputPort;
import dev.amir.resourceservice.application.port.output.StorageServiceOutputPort;
import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.vo.ResourceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceDataStorageServiceImpl implements ResourceDataStorageService {
    private final ResourceDataStorageOutputPort resourceDataStorageOutputPort;
    private final StorageServiceOutputPort storageServiceOutputPort;

    @Value("${services.storage-service.type.temporal}")
    private String storageTypeTemporal;

    @Value("${services.storage-service.type.permanent}")
    private String storageTypePermanent;

    @Override
    public void uploadResource(Resource resourceId, byte[] resourceData) {
        var storageInformation = getStorageInformationByType(getAllStorageInformation(), storageTypeTemporal);
        resourceDataStorageOutputPort.uploadResource(resourceId, resourceData, storageInformation);
    }

    @Override
    public byte[] downloadResource(Resource resource) {
        var storageType = ResourceStatus.PERMANENT.equals(resource.getStatus()) ? storageTypePermanent : storageTypeTemporal;
        var storageInformation = getStorageInformationByType(getAllStorageInformation(), storageType);
        return resourceDataStorageOutputPort.downloadResource(resource, storageInformation);
    }

    @Override
    public byte[] downloadResource(Resource resource, ByteRange byteRange) {
        var storageType = ResourceStatus.PERMANENT.equals(resource.getStatus()) ? storageTypePermanent : storageTypeTemporal;
        var storageInformation = getStorageInformationByType(getAllStorageInformation(), storageType);
        return resourceDataStorageOutputPort.downloadResource(resource, byteRange, storageInformation);
    }

    @Override
    public void deleteResources(Collection<Resource> resources) {
        var resourcesGroupedByStatus = resources.stream().collect(Collectors.groupingBy(Resource::getStatus));
        var storageInformationList = getAllStorageInformation();
        var storageInformationByStatus = resourcesGroupedByStatus
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                        status -> status,
                        status -> getStorageInformationByType(storageInformationList, status.name())
                ));
        resourcesGroupedByStatus.forEach((status, resourceList) -> deleteResources(resourceList, storageInformationByStatus.get(status)));
    }

    @Override
    public void moveResource(Resource resource) {
        var storageInformationList = getAllStorageInformation();
        var sourceStorageInfo = getStorageInformationByType(storageInformationList, storageTypeTemporal);
        var destinationStorageInfo = getStorageInformationByType(storageInformationList, storageTypePermanent);
        resourceDataStorageOutputPort.moveResource(resource, sourceStorageInfo, destinationStorageInfo);
    }

    private StorageInformation getStorageInformationByType(Collection<StorageInformation> storageInformationList, String storageType) {
        return storageInformationList
                .stream()
                .filter(storageInformation -> storageType.equals(storageInformation.getStorageType()))
                .findFirst()
                .orElseThrow(() -> new StorageInformationNotFoundException(storageType));
    }

    private Collection<StorageInformation> getAllStorageInformation() {
        var storageInformationList = storageServiceOutputPort.getAllStorageInformation();
        return CollectionUtils.isEmpty(storageInformationList) ? Collections.emptyList() : storageInformationList;
    }

    private void deleteResources(Collection<Resource> resources, StorageInformation storageInformation) {
        resourceDataStorageOutputPort.deleteResources(resources, storageInformation);
    }
}
