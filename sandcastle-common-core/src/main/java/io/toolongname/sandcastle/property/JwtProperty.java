package io.toolongname.sandcastle.property;

import org.springframework.boot.context.properties.ConfigurationProperties;


@Deprecated
@ConfigurationProperties(prefix = "jwt")
public record JwtProperty(String issuer,
                          int validityPeriod,
                          String base64Secret,
                          String algorithm) {
    public JwtProperty {
    }
}
