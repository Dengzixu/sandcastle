package io.toolongname.sandcastle.component.impl;


import io.toolongname.sandcastle.component.StorageComponent;
import io.toolongname.sandcastle.property.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class StorageComponentImpl implements StorageComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageComponentImpl.class);

    private final S3Client s3Client;
    private final StorageProperties storageProperties;
    private final AwsBasicCredentials credential;
    private final S3Configuration serviceConfiguration;

    private final StringRedisTemplate stringRedisTemplate;

    public StorageComponentImpl(StorageProperties storageProperties,
                                StringRedisTemplate stringRedisTemplate) {
        this.storageProperties = storageProperties;
        this.stringRedisTemplate = stringRedisTemplate;

        this.credential = AwsBasicCredentials.create(storageProperties.accessKeyId(),
                storageProperties.accessKeySecret());

        this.serviceConfiguration = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        s3Client = S3Client.builder()
                .endpointOverride(URI.create(storageProperties.endpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(credential))
                .region(Region.of("auto"))
                .serviceConfiguration(serviceConfiguration)
                .build();
    }

    @Override
    public void save(InputStream stream, String objectKey) {

    }

    @Override
    public void save(byte[] bytes, String objectKey) {

    }

    @Override
    public void save(File file, String objectKey) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.storageProperties.bucketName())
                .key(objectKey)
                .build();

        RequestBody requestBody = RequestBody.fromFile(file);

        PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);
    }

    @Override
    public List<String> listAll() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(this.storageProperties.bucketName())
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);


        return response.contents().stream().map(String::valueOf).toList();
    }


    @Override
    public String getPresignUrl(String objectKey) {
        if (stringRedisTemplate.hasKey(objectKey)) {
            LOGGER.info("对象: [{}] 命中缓存", objectKey);
            return stringRedisTemplate.opsForValue().get(objectKey);
        }

        S3Presigner presigner = S3Presigner.builder()
                .endpointOverride(URI.create(storageProperties.endpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(credential))
                .region(Region.of("auto"))
                .serviceConfiguration(serviceConfiguration)
                .build();

        try (presigner) {
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(builder ->
                            builder.bucket(this.storageProperties.bucketName())
                                    .key(objectKey)
                                    .build())
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);


            String objectURL = presignedGetObjectRequest.url().toString();
            stringRedisTemplate.opsForValue()
                    .set(objectKey, presignedGetObjectRequest.url().toString(), 8, TimeUnit.MINUTES);

            return objectURL;
        }
    }
}
