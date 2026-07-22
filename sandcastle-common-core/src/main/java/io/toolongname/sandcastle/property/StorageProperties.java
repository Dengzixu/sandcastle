package io.toolongname.sandcastle.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage.s3")
public record StorageProperties(String accessKeyId,
                                String accessKeySecret,
                                String bucketName,
                                String endpoint) {
}
