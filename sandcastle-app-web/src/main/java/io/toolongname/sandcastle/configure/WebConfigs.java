package io.toolongname.sandcastle.configure;

import io.toolongname.sandcastle.interceptor.TurnstileInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigs implements WebMvcConfigurer {

    private final TurnstileInterceptor turnstileInterceptor;

    public WebConfigs(TurnstileInterceptor turnstileInterceptor) {
        this.turnstileInterceptor = turnstileInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(turnstileInterceptor)
                .excludeHttpMethods(HttpMethod.OPTIONS)
                .addPathPatterns("/user/api/v1/login")
                .addPathPatterns("/user/api/v1/register");

//        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
