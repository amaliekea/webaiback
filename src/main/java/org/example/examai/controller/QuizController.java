package org.example.examai.controller;

import org.example.examai.dto.StudyQuestion;
import org.example.examai.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = "*") //giver alle adgang til at hente data fra min backend
@RestController //Gør klassen til en web-controller, der kan håndtere HTTP-requests.
public class QuizController {
    @Autowired
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @Value("${openai.api.key}") //henter nøgler fra application.prop
    private String openapikey;

    @Value("${QUIZAPIKEY}")
    private String quizapikey;


    @GetMapping("/key")
    public String key() {
        return openapikey;
    }


    @PostMapping("/study-helper")
    public String studyHelper(@RequestBody StudyQuestion question) {
        System.out.println("studyhelper ackend ramt");
        System.out.println("QUESTION SEND " + question);
        System.out.println(quizService.explainTopicWithGPT(question));
        return quizService.explainTopicWithGPT(question);
    }

//        @GetMapping("/test")
//    public Map<String, Object> test() {
//        Map<String, Object> testmap = medAIService.promptWebAI();
//        return testmap;
//    }

}

