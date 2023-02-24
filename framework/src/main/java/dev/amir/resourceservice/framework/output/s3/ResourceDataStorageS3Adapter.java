package dev.amir.resourceservice.framework.output.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import dev.amir.resourceservice.application.port.output.ResourceDataStorageOutputPort;
import dev.amir.resourceservice.domain.entity.ByteRange;
import dev.amir.resourceservice.domain.entity.Resource;
import dev.amir.resourceservice.domain.exception.UnexpectedResourceException;
import dev.amir.resourceservice.domain.vo.ContentLength;
import dev.amir.resourceservice.domain.vo.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public void uploadResource(Resource resource, byte[] resourceData) {
        try {
            var objectKey = buildObjectKey(resource);
            var inputStream = new ByteArrayInputStream(resourceData);
            var metadata = buildMetadata(resource.getContentType(), resource.getContentLength());
            log.info("Uploading ResourceData with ObjectKey: [{}] to S3", objectKey);
            var result = s3Client.putObject(bucketName, objectKey, inputStream, metadata);
            if (Objects.isNull(result) || !StringUtils.hasText(result.getETag())) {
                throw new UnexpectedResourceException("Failed Operation: ResourceData was not uploaded to S3");
            }
        } catch (AmazonClientException | NullPointerException exception) {
            throw new UnexpectedResourceException(exception);
        }
        log.info("ResourceData was successfully uploaded");
    }

    @Override
    public byte[] downloadResource(Resource resource) {
        var objectKey = buildObjectKey(resource);
        var request = new GetObjectRequest(bucketName, objectKey);
        return downloadResource(request);
    }

    @Override
    public byte[] downloadResource(Resource resource, ByteRange byteRange) {
        var objectKey = buildObjectKey(resource);
        long fixedStart = getFixedStart(resource, byteRange);
        long fixedEnd = getFixedEnd(resource, fixedStart, byteRange);
        var request = new GetObjectRequest(bucketName, objectKey);
        request.setRange(fixedStart, fixedEnd);
        return downloadResource(request);
    }

    @Override
    public void deleteResources(Collection<Resource> resources) {
        if (CollectionUtils.isEmpty(resources)) {
            return;
        }

        try {
            var objectKeys = buildObjectKeys(resources);
            var deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
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

    String buildObjectKey(Resource resource) {
        return resource.getPath() + "/" + resource.getName();
    }

    private List<String> buildObjectKeys(Collection<Resource> resources) {
        return resources.stream().map(this::buildObjectKey).toList();
    }
}
