package org.example.medai.service;

import org.example.medai.dto.Message;
import org.example.medai.dto.RequestDTO;
import org.example.medai.dto.ResponseDTO;
import org.example.medai.dto.StomachInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MedAIService {

    private final WebClient openAiWebClient;
    private final WebClient pubMedWebClient;

    @Autowired
    public MedAIService(WebClient.Builder webClientBuilder) {
        this.openAiWebClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
        this.pubMedWebClient = webClientBuilder.baseUrl("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/").build();
    }

    @Value("${openai.api.key}")
    private String openapikey;

    public Map<String, Object> promptWebAI(String question, String pubMedData) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setModel("gpt-3.5-turbo");
        requestDTO.setTemperature(1.0);
        requestDTO.setMaxTokens(200);

        List<Message> lstMessages = new ArrayList<>();
        lstMessages.add(new Message("system", "You are a helpful medical assistant. Here is some PubMed data: " + pubMedData));
        lstMessages.add(new Message("user", question));

        requestDTO.setMessages(lstMessages);

        // Send forespørgsel til OpenAI API
        ResponseDTO response = openAiWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(openapikey))
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .block();

        Map<String, Object> map = new HashMap<>();
        map.put("Choices", response.getChoices());
        return map;
    }

    // Hent PubMed data baseret på symptomer
    public String fetchPubMedData(String query) {
        String url = String.format("esearch.fcgi?db=pubmed&term=%s", query); // søgning
        return pubMedWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // Kombiner OpenAI og PubMed svar
    public Map<String, Object> askAboutStomachProblem(StomachInput input) {
        String prompt = String.format("A patient has stomach pain located at %s, for %s. Symptoms include %s. Recently ate %s. Recent illness: %s.",
                input.getPainLocation(), input.getDuration(), input.getSymptoms(), input.getRecentFood(), input.getRecentIllness());

        // Hent PubMed data baseret på symptomer
        String pubMedData = fetchPubMedData(input.getSymptoms());

        if (pubMedData == null || pubMedData.isEmpty()) {
            pubMedData = "No specific research found. Please refine your symptoms query.";
        }

        return promptWebAI(prompt, pubMedData);
    }
}