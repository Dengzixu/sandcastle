package io.toolongname.sandcastle.property;

import org.springframework.boot.context.properties.ConfigurationProperties;



@ConfigurationProperties(prefix = "jwt")
public record JwtProperty(String issuer,
                          int validityPeriod,
                          String base64Secret,
                          String algorithm) {
    public JwtProperty {
    }
}
