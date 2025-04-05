package org.example.medai.controller;

import org.example.medai.dto.StudyQuestion;
import org.example.medai.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
public class QuizController {
    @Autowired
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }
    @Value("${openai.api.key}")
    private String openapikey;

    @Value("${QUIZAPIKEY}")
    private String quizapikey;



    @GetMapping("/key")
    public String key() {
        return openapikey;
    }

//    @GetMapping("/test")
//    public Map<String, Object> test() {
//        Map<String, Object> testmap = medAIService.promptWebAI();
//        return testmap;
//    }

    @PostMapping("/study-helper")
    public Map<String, Object> studyHelper(@RequestBody StudyQuestion question) {
        return quizService.explainTopicWithGPT(question);
    }

}

