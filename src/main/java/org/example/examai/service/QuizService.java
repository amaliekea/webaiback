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

//Denne klasse håndterer quiz-funktioner ved hjælp af AI.
// Den bruger OpenAI til at forklare emner, og quizapi.io til at hente quizspørgsmål.
// Bruges af controlleren til at give svar til frontend.

@Service
public class QuizService {

    private final OpenAIService openAIService;
    private final WebClient quizApiWebClient;

    @Autowired
    public QuizService(WebClient.Builder webClientBuilder, OpenAIService openAIService) {
        this.openAIService = openAIService; //kalder OpenAI’s chat/completions endpoint.
        this.quizApiWebClient = webClientBuilder.baseUrl("https://quizapi.io/api/v1").build(); // kalder quizAPI.io’s root-URL.
    }
    @Autowired
    ArticleApiService articleApiService; //for at bruge vores metode til at hente artikler

    @Value("${OPENAPIKEY}")
    private String openapikey;

    @Value("${QUIZAPIKEY}")
    private String quizapikey;


    public String explainTopicWithGPT(StudyQuestion question) {
        List<Message> messages = new ArrayList<>();

        String prompt = "explain the topic '" + question.getTopic() + "' for a student on " + question.getLevel() + "-niveau.";
        String level = switch (question.getLevel().toLowerCase()) {
            case "low" -> "Easy";
            case "medium" -> "Medium";
            case "high" -> "Hard";
            default -> "Medium";
        };

        if (question.isIncludeQuiz()) {
            String quizData = fetchQuizQuestions(question.getTopic(), level);
            prompt += " Here is a quiz about the subject: " + quizData;
            prompt += " Use them as inspiration and make 2 extra new quizquestions at the end.";
        }

        String article = articleApiService.fetchArticle(question.getTopic());
        prompt += " Here is an article about the topic:\n\"" + article + "\"\nPlease include this knowledge in your explanation.";

        messages.add(new Message("system", "you are a helpful tutor."));
        messages.add(new Message("user", prompt));

        return openAIService.getResponseFromOpenAI(messages);
    }

    public String fetchQuizQuestions(String category, String difficulty) {
        try {
            return quizApiWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/questions")
                            .queryParam("category", category)
                            .queryParam("difficulty", difficulty)
                            .queryParam("limit", 3)
                            .build())
                    .header("X-Api-Key", quizapikey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            return "Could not fetch quiz questions: " + e.getMessage();
        }
    }
}