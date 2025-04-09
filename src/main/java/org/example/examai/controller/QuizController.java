package org.example.examai.controller;

import org.example.examai.dto.StudyQuestion;
import org.example.examai.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") //giver alle adgang til at hente data fra min backend
@RestController //Gør klassen til en web-controller, der kan håndtere HTTP-requests.
public class QuizController {

    @Autowired //burde laves om da autowired er deprecated
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @Value("${OPENAPIKEY}") //henter nøgler fra pplication.prop
    private String openapikey;


    @GetMapping("/key") // til test af nøgle
    public String key() {
        return openapikey;
    }

    @PostMapping("/study-helper")
    public String studyHelper(@RequestBody StudyQuestion question) { //vi tager et studyquestion ind
        return quizService.explainTopicWithGPT(question); //kalder metoden i service der retunerer en streng
    }
//
//    @PostMapping("/study-helper/{model}")
//    public String studyHelper(@RequestBody StudyQuestion question, @PathVariable String model) { //vi tager et studyquestion ind
//        return quizService.explainTopic(question, model); //kalder metoden i service der retunerer en streng
//    }
}
