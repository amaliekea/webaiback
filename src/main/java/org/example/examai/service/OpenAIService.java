package org.example.examai.service;

import org.example.examai.dto.Message;
import org.example.examai.dto.RequestDTO;
import org.example.examai.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Denne klasse håndterer kommunikation med OpenAI's API.
 * Den sender brugerens spørgsmål som en liste af beskeder og retunerer svar som streng
 */
@Service
public class OpenAIService {

    private final WebClient openAiWebClient;

    public OpenAIService(WebClient.Builder webClientBuilder) {
        this.openAiWebClient = webClientBuilder.baseUrl("https://api.mistral.ai/v1/chat/completions").build(); //OpenAI's chat/completions endpoint.
    }

    @Value("${_OPENAPIKEY}")
    private String openapikey;

    public String getResponseFromOpenAI(List<Message> messages) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setModel("mistral-small-latest"); // hvilken model vi bruger
        requestDTO.setTemperature(0.7); // hvor kreativt svaret skal være
        requestDTO.setMaxTokens(800); // længde på svaret
        requestDTO.setTopP(1.0); // sandsynlighedsindstilling
        requestDTO.setFrequencyPenalty(0.2); // straf for gentagelser
        requestDTO.setPresencePenalty(0.3); // straf for at gentage allerede nævnte ting
        requestDTO.setMessages(messages); // vores beskeder

        // Sender request og modtager svar som ResponseDTO
        ResponseDTO response = openAiWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(openapikey)) // tilføjer API-nøglen
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .block(); // gør det synkront

        // Returnerer svaret som tekst
        return response.getChoices().getFirst().getMessage().getContent();
    }
}

