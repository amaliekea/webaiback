package org.example.examai.service;

import org.example.examai.dto.Message;
import org.example.examai.dto.RequestDTO;
import org.example.examai.dto.ResponseDTO;
import org.example.examai.dto.StudyQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Denne klasse håndterer quiz-funktioner ved hjælp af AI.
 * Den bruger OpenAI til at forklare emner, og quizapi.io til at hente quizspørgsmål.
 * Bruges af controlleren til at give svar til frontend.
 */
@Service
public class QuizService {

    private final WebClient openAiWebClient;
    private final WebClient quizApiWebClient;

    @Autowired
    public QuizService(WebClient.Builder webClientBuilder) {
        this.openAiWebClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
        this.quizApiWebClient = webClientBuilder.baseUrl("https://quizapi.io/api/v1").build();
    }

    @Value("${openai.api.key}")
    private String openapikey;

    @Value("${quizapi.api.key}")
    private String quizapikey;


    /**
     * Kalder OpenAI og genererer en forklaring baseret på brugerens spørgsmål.
     *
     * @param question Brugerens input med emne, niveau og evt. quizønske
     * @return Et map med AI'ens svar (Choices)
     */
    public String explainTopicWithGPT(StudyQuestion question) {
        List<Message> lstMessages = new ArrayList<>();

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setModel("gpt-3.5-turbo");
        requestDTO.setTemperature(0.7);
        requestDTO.setMaxTokens(800);
        requestDTO.setTopP(1.0);
        requestDTO.setFrequencyPenalty(0.2);
        requestDTO.setPresencePenalty(0.3);
        requestDTO.setMessages(lstMessages);

        String basePrompt = "explain the topic '" + question.getTopic() + "' on danish for a student on" + question.getLevel() + "-niveau.";
        // Vi vil gerne map brugerens niveau/level til sværhedsgraden af svar fra API's
        String difficultyLevel = question.getLevel();

        if (difficultyLevel != null) {
             difficultyLevel = switch (question.getLevel().toLowerCase()) {
                case "low" -> "Easy";
                case "medium" -> "Medium";
                case "high" -> "Hard";
                default -> "Medium";
            };
        }

        // Brug quizapi.io data som kontekst hvis inkluderet
        if (question.isIncludeQuiz()) {
            String quizData = fetchQuizQuestions(question.getTopic(), difficultyLevel);
            basePrompt += " Here a ome questions about the subject: " + quizData;
            basePrompt += " Use them as inspiration and make 2 new quizquestions at last.";
        }

        lstMessages.add(new Message("system", "you are a helpfull tutor."));
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
        
        String gptresponse = response.getChoices().getFirst().getMessage().getContent();
        
        return gptresponse;
    }

//denne metode bruges til at hente quic fra vores api
    public String fetchQuizQuestions(String category, String difficulty) {
        return quizApiWebClient.get() //vi sender en get
                .uri(uriBuilder -> uriBuilder
                        .path("/questions")
                        .queryParam("category", category)
                        .queryParam("difficulty", difficulty)
                        .queryParam("limit", 3) //henter 3 spørgsmål
                        .build())
                .header("X-Api-Key", quizapikey)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}