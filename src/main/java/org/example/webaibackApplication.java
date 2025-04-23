package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class webaibackApplication {
    //YAML api-dokumentation hentes her: http://localhost:8080/v3/api-docs.yaml
    //JSON api-dokumentation hentes her: http://localhost:8080/v3/api-docs
    //swagger: http://localhost:8080/swagger-ui.html

    public static void main(String[] args) {
        SpringApplication.run(webaibackApplication.class, args);
    }

}
