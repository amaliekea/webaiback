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

    private final WebClient openAiWebClient;
    private final WebClient mistralWebClient;
    private final WebClient quizApiWebClient;

    @Autowired
    public QuizService(WebClient.Builder webClientBuilder) {
        this.openAiWebClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build(); //kalder OpenAI’s chat/completions endpoint.
        this.mistralWebClient = webClientBuilder.baseUrl("https://api.mistral.ai/v1/chat/completions").build(); // kalder quizAPI.io’s root-URL.
        this.quizApiWebClient = webClientBuilder.baseUrl("https://quizapi.io/api/v1").build(); // kalder quizAPI.io’s root-URL.
    }
    @Autowired
    ArticleApiService articleApiService; //for at bruge vores metode til at hente artikler

    @Value("${OPENAPIKEY}")
    private String openapikey;

    @Value("${MISTRALAPIKEY}")
    private String mistralApiKey;

    @Value("${QUIZAPIKEY}")
    private String quizapikey;


    public String explainTopic(StudyQuestion question, String model) { //modtager et studyquestion oprindeligt fra frontend
        List<Message> lstMessages = new ArrayList<>(); //opretter liste af beskeder som openai får

        //sætter parametre for modellen i vores request, for at modelere svaret
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setModel(model);
        requestDTO.setTemperature(0.7);
        requestDTO.setMaxTokens(800);
        requestDTO.setTopP(1.0);
        requestDTO.setFrequencyPenalty(0.2);
        requestDTO.setPresencePenalty(0.3);
        requestDTO.setMessages(lstMessages);

        //dynamisk byggelse af promt ud fra brugerens input
        String basePrompt = "explain the topic '" + question.getTopic() + "for a student on" + question.getLevel() + "-niveau.";
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

        //tilføj quiz hvis brugeren har indtastet true
        if (question.isIncludeQuiz()) {
            String quizData = fetchQuizQuestions(question.getTopic(), difficultyLevel);
            basePrompt += " Here a quiz about the subject: " + quizData;
            basePrompt += " Use them as inspiration and make 2 ekstra new quizquestions at last.";
        }
        //tilføj artikel
        String article = articleApiService.fetchArticle(question.getTopic());
        basePrompt += " Here you can read more about the topic:\n\"" + article + "\"\nPlease include this knowledge in your explanation.";

        System.out.println("Artikel fundet: " + article);

        lstMessages.add(new Message("system", "you are a helpfull tutor."));
        lstMessages.add(new Message("user", basePrompt));

        requestDTO.setMessages(lstMessages);

        WebClient client = determineWebClient(model);

        //nedenstående sender request til openAi
        ResponseDTO response = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(determineApiKey(model)))
                .bodyValue(requestDTO)
                .retrieve()
                //svaret mappes til response
                .bodyToMono(ResponseDTO.class)
                .block();


        //svaret retuneres som en simpel streng
        String gptresponse = response.getChoices().getFirst().getMessage().getContent();

        return gptresponse;
    }

    //denne metode bruges til at hente quiz fra vores api
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



        private String determineApiKey(String model) {
        switch (model) {
            case "gpt-3.5-turbo":
                return openapikey;
            case "mistral-small-latest":
                return mistralApiKey;
            default:
                return "";
        }
    }

    private WebClient determineWebClient(String model) {
        switch (model) {
            case "gpt-3.5-turbo":
                return openAiWebClient;
            case "mistral-small-latest":
                return mistralWebClient;
            default:
                return mistralWebClient;
        }
    }
}