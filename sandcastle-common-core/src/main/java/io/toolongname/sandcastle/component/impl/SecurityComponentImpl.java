package io.toolongname.sandcastle.component.impl;


import io.toolongname.sandcastle.component.SecurityComponent;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class SecurityComponentImpl implements SecurityComponent {

    private static final String secret = "1x0000000000000000000000000000000AA";

    @Override
    public void turnstileVerify(String token) {
        final URI uri = URI.create("https://challenges.cloudflare.com/turnstile/v0/siteverify");

        Map<String, String> bodyMap = new LinkedHashMap<>();

        bodyMap.put("secret", secret);
        bodyMap.put("response", token);

        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder(uri)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(bodyMap)))
                .setHeader("Content-Type", "application/json")
                .build();


        JsonNode node;
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            node = objectMapper.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (!node.get("success").asBoolean(false)) {
            throw new RuntimeException();
        }

        try (httpClient) {
            // do nothing
        }
    }


}
