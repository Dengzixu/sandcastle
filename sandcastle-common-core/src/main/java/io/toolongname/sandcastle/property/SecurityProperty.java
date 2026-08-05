package io.toolongname.sandcastle.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperty(Turnstile turnstile, Jwt jwt) {
    public record Turnstile(String secretKey) {
    }

    public record Jwt(String issuer,
                      int validityPeriod,
                      String base64Secret,
                      String algorithm) {
    }
}
