package io.toolongname.sandcastle.support.configurer;

import io.toolongname.sandcastle.support.annotation.DecodeJwtArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final DecodeJwtArgumentResolver decodeJwtArgumentResolver;

    public WebConfig(DecodeJwtArgumentResolver decodeJwtArgumentResolver) {
        this.decodeJwtArgumentResolver = decodeJwtArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(decodeJwtArgumentResolver);
    }
}
