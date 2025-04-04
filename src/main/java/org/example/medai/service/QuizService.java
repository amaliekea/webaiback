package org.example.medai.service;

import org.example.medai.dto.Message;
import org.example.medai.dto.RequestDTO;
import org.example.medai.dto.ResponseDTO;
import org.example.medai.dto.StudyQuestion;
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
public class QuizService {

    private final WebClient openAiWebClient;
    private final WebClient pubMedWebClient;
    private final WebClient quizApiWebClient;

    @Autowired
    public QuizService(WebClient.Builder webClientBuilder) {
        this.openAiWebClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
        this.pubMedWebClient = webClientBuilder.baseUrl("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/").build();
        this.quizApiWebClient = webClientBuilder.baseUrl("https://quizapi.io/api/v1").build();
    }


    @Value("${openai.api.key}")
    private String openapikey;

    @Value("${quizapi.api.key}")
    private String quizapikey;


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

    public Map<String, Object> explainTopicWithGPT(StudyQuestion question) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setModel("gpt-3.5-turbo");
        requestDTO.setTemperature(0.7);
        requestDTO.setMaxTokens(500);

        List<Message> lstMessages = new ArrayList<>();

        String basePrompt = "Forklar emnet '" + question.getTopic() + "' som om det blev forklaret til en " + question.getLevel() + ".";

        // Brug quizapi.io data som kontekst hvis inkluderet
        if (question.isIncludeQuiz()) {
            String quizData = fetchQuizQuestions("code"); // kategori kan ændres
            basePrompt += " Her er nogle spørgsmål om emnet: " + quizData;
            basePrompt += " Brug dem som inspiration og lav 2 nye quizspørgsmål til sidst.";
        }

        lstMessages.add(new Message("system", "Du er en hjælpsom underviser."));
        lstMessages.add(new Message("user", basePrompt));

        requestDTO.setMessages(lstMessages);

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

    public String fetchQuizQuestions(String category) {
        return quizApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/questions")
                        .queryParam("category", category)
                        .queryParam("limit", 3)
                        .build())
                .header("X-Api-Key", quizapikey)
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }


}