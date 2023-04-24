package dev.amir.resourceservice.framework.output.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import dev.amir.resourceservice.application.dto.StorageInformation;
import dev.amir.resourceservice.application.port.output.ResourceDataStorageOutputPort;
import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.exception.UnexpectedResourceException;
import dev.amir.resourceservice.domain.vo.ContentLength;
import dev.amir.resourceservice.domain.vo.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceDataStorageS3Adapter implements ResourceDataStorageOutputPort {
    public static final String PATH_DELIMITER = "/";
    private final AmazonS3 s3Client;

    @Override
    public void uploadResource(Resource resource, byte[] resourceData, StorageInformation storageInformation) {
        try {
            createBucketIfNotExist(storageInformation);
            var objectKey = buildObjectKey(resource, storageInformation);
            var inputStream = new ByteArrayInputStream(resourceData);
            var metadata = buildMetadata(resource.getContentType(), resource.getContentLength());
            log.info("Uploading ResourceData with ObjectKey: [{}] to S3", objectKey);
            var result = s3Client.putObject(storageInformation.getBucket(), objectKey, inputStream, metadata);
            if (Objects.isNull(result) || !StringUtils.hasText(result.getETag())) {
                throw new UnexpectedResourceException("Failed Operation: ResourceData was not uploaded to S3");
            }
        } catch (AmazonClientException | NullPointerException exception) {
            throw new UnexpectedResourceException(exception);
        }
        log.info("ResourceData was successfully uploaded");
    }

    @Override
    public byte[] downloadResource(Resource resource, StorageInformation storageInformation) {
        var objectKey = buildObjectKey(resource, storageInformation);
        var request = new GetObjectRequest(storageInformation.getBucket(), objectKey);
        return downloadResource(request);
    }

    @Override
    public byte[] downloadResource(Resource resource, ByteRange byteRange, StorageInformation storageInformation) {
        var objectKey = buildObjectKey(resource, storageInformation);
        long fixedStart = getFixedStart(resource, byteRange);
        long fixedEnd = getFixedEnd(resource, fixedStart, byteRange);
        var request = new GetObjectRequest(storageInformation.getBucket(), objectKey);
        request.setRange(fixedStart, fixedEnd);
        return downloadResource(request);
    }

    @Override
    public void deleteResources(Collection<Resource> resources, StorageInformation storageInformation) {
        if (CollectionUtils.isEmpty(resources)) {
            return;
        }

        try {
            var objectKeys = buildObjectKeys(resources, storageInformation);
            var deleteObjectsRequest = new DeleteObjectsRequest(storageInformation.getBucket());
            deleteObjectsRequest.withKeys(objectKeys.stream().map(DeleteObjectsRequest.KeyVersion::new).toList());
            var result = s3Client.deleteObjects(deleteObjectsRequest);
            result.getDeletedObjects().stream()
                    .map(DeleteObjectsResult.DeletedObject::getKey)
                    .filter(deletedObjectKey -> !objectKeys.contains(deletedObjectKey))
                    .forEach(deletedObjectKey -> log.error("Resource [{}] does not exist in S3", deletedObjectKey));

        } catch (AmazonClientException | NullPointerException exception) {
            throw new UnexpectedResourceException(exception);
        }
    }

    @Override
    public void moveResource(Resource resource, StorageInformation sourceStorageInfo, StorageInformation destinationStorageInfo) {
        try {
            var sourceKey = buildObjectKey(resource, sourceStorageInfo);
            var destinationKey = buildObjectKey(resource, destinationStorageInfo);
            createBucketIfNotExist(destinationStorageInfo);
            s3Client.copyObject(sourceStorageInfo.getBucket(), sourceKey, destinationStorageInfo.getBucket(), destinationKey);
            s3Client.deleteObject(sourceStorageInfo.getBucket(), sourceKey);
        } catch (AmazonClientException | NullPointerException exception) {
            throw new UnexpectedResourceException(exception);
        }
    }

    public void createBucketIfNotExist(StorageInformation storageInformation) {
        if (!s3Client.doesBucketExistV2(storageInformation.getBucket())) {
            s3Client.createBucket(storageInformation.getBucket());
        }
    }

    private long getFixedStart(Resource resource, ByteRange byteRange) {
        long length = resource.getContentLength().getValue();
        return byteRange.getStart() == null || byteRange.getStart() < 0L ?
                length - byteRange.getEnd() :
                byteRange.getStart();
    }


    private long getFixedEnd(Resource resource, Long start, ByteRange byteRange) {
        long length = resource.getContentLength().getValue();
        return byteRange.getEnd() == null || byteRange.getEnd() < 0L || byteRange.getEnd() + start > length ?
                length :
                byteRange.getEnd() + start;
    }

    private byte[] downloadResource(GetObjectRequest request) {
        log.info("Downloading ResourceData with ObjectKey: [{}] to S3", request.getKey());
        try {
            S3Object response = s3Client.getObject(request);
            return IOUtils.toByteArray(response.getObjectContent());
        } catch (Exception exception) {
            throw new UnexpectedResourceException(exception);
        }
    }

    private ObjectMetadata buildMetadata(ContentType contentType, ContentLength contentLength) {
        var objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType.getValue());
        objectMetadata.setContentLength(contentLength.getValue());
        return objectMetadata;
    }

    String buildObjectKey(Resource resource, StorageInformation storageInformation) {
        return String.join(
                PATH_DELIMITER,
                storageInformation.getPath(),
                resource.getPath(),
                resource.getName().getValue()
        );
    }

    private List<String> buildObjectKeys(Collection<Resource> resources, StorageInformation storageInformation) {
        return resources.stream().map(resource -> buildObjectKey(resource, storageInformation)).toList();
    }
}
