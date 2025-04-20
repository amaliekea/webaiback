package org.example.examai.service;

import org.example.examai.dto.Message;

import org.example.examai.dto.StudyQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviceklasse der håndterer quiz-relateret logik.
 * Henter quizspørgsmål fra quizapi.io og forklaringer fra OpenAI.
 * Tilføjer også en artikel fra en artikel-API til svaret.
 */

@Service
public class QuizService {

    private final OpenAIService openAIService;
    private final WebClient quizApiWebClient;
    /**
     * Constructor med dependency injection af OpenAIService og WebClient.
     *
     * @param openAIService    Service der håndterer kald til OpenAI.
     * @param webClientBuilder WebClient-builder til at oprette forbindelser.
     */
    @Autowired
    public QuizService(OpenAIService openAIService, WebClient.Builder webClientBuilder) {
        this.openAIService = openAIService;
        this.quizApiWebClient = webClientBuilder.baseUrl("https://quizapi.io/api/v1").build(); // kalder quizAPI.io's root-URL.
    }

    @Autowired
    ArticleApiService articleApiService; // Service til at hente artikler fra ekstern API

    @Value("${QUIZAPIKEY}")
    private String quizapikey;

    /**
     * Genererer en forklaring på et emne ved hjælp af OpenAI, og tilføjer evt. quiz og artikel.
     *
     * @param question Et objekt der indeholder brugerens emne, niveau og quiz-ønske.
     * @return En forklarende tekst genereret af OpenAI.
     */
    public String explainTopicWithGPT(StudyQuestion question) {
        List<Message> lstMessages = new ArrayList<>();

        String basePrompt = "explain the topic '" + question.getTopic() + " for a student on a " + question.getLevel() + "-niveau.";

        String difficultyLevel = question.getLevel();

        if (question.isIncludeQuiz()) {
            String quizData = fetchQuizQuestions(question.getTopic(), difficultyLevel);
            basePrompt += " Here is a quiz about the subject: " + quizData;
            basePrompt += " Use them as inspiration and make 2 ekstra new quizquestions at last.";
        }

        String article = articleApiService.fetchArticle(question.getTopic());
        basePrompt += "\nBelow is an article. You MUST always end your answers with, here is an article if you want to read more about technology:\n" +
                "\"" + article + "\"\n" +
                "Be sure to refer to this article explicitly in your answer.";

        lstMessages.add(new Message("system", "You are a helpful tutor."));
        lstMessages.add(new Message("user", basePrompt));

        return openAIService.getResponseFromOpenAI(lstMessages);
    }

    /**
     * Henter quizspørgsmål fra quizapi.io baseret på emne og sværhedsgrad.
     *
     * @param category   Emne/område for quizspørgsmål.
     * @param difficulty Niveau fx easy, medium, hard.
     * @return En JSON-streng med quizspørgsmål.
     */
    public String fetchQuizQuestions(String category, String difficulty) {
        return quizApiWebClient.get() //vi sender en get
                .uri(uriBuilder -> uriBuilder
                        .path("/questions")
                        .queryParam("category", category)
                        .queryParam("difficulty", difficulty) //sætter sværhedsgraden easy, medium..
                        .queryParam("limit", 3) //henter 3 spørgsmål
                        .build())
                .header("X-Api-Key", quizapikey)
                .retrieve()
                .bodyToMono(String.class) //retunerer JSON som en streng
                .block();
    }
}