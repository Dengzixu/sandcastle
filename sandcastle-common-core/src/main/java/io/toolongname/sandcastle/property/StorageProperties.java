package io.toolongname.sandcastle.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "storage.s3")
public record StorageProperties(String accessKeyId,
                                String accessKeySecret,
                                String bucketName,
                                String endpoint) {
}
