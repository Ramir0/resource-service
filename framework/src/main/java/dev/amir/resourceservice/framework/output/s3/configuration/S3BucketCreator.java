package dev.amir.resourceservice.framework.output.s3.configuration;

import dev.amir.resourceservice.domain.profile.Profiles;
import com.amazonaws.services.s3.AmazonS3;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("!" + Profiles.TEST)
@Configuration
@RequiredArgsConstructor
public class S3BucketCreator {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String defaultBucketName;

    @PostConstruct
    public void createBucket() {
        if (!s3Client.doesBucketExistV2(defaultBucketName)) {
            s3Client.createBucket(defaultBucketName);
        }
    }
}
