package io.toolongname.sandcastle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SandcastleAppWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandcastleAppWebApplication.class, args);
    }

}
