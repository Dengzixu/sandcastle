package io.toolongname.sandcastle.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperty(Turnstile turnstile) {
    public record Turnstile(String secretKey) {

    }
}
