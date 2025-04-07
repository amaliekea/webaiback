package org.example.examai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    //Denne klasse forbereder min applikation på at sende HTTP-forespørgsler
    //eksempel på brug af webclient:
    //  .uri("https://api.example.com/person")
    //.header("Content-Type", "application/json")
    //.retrieve()
    //.bodyToMono(Void.class)
    //.block();

    @Bean
    public WebClient.Builder webClientBuilder() { //metoden opretter og retunerer en ny instans af webclient.builder
        return WebClient.builder();
    }

}
