package org.example.examai.service;

import org.example.examai.dto.Message;
import org.example.examai.dto.RequestDTO;
import org.example.examai.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class OpenAIService {

    private final WebClient openAiWebClient;

    public OpenAIService(WebClient.Builder webClientBuilder) {
        this.openAiWebClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
    }

    @Value("${OPENAPIKEY}")
    private String openapikey;

    public String getResponseFromOpenAI(List<Message> messages) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setModel("gpt-3.5-turbo");
        requestDTO.setTemperature(0.7);
        requestDTO.setMaxTokens(800);
        requestDTO.setTopP(1.0);
        requestDTO.setFrequencyPenalty(0.2);
        requestDTO.setPresencePenalty(0.3);
        requestDTO.setMessages(messages);

        ResponseDTO response = openAiWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(openapikey))
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .block();

        return response.getChoices().getFirst().getMessage().getContent();
    }
}

