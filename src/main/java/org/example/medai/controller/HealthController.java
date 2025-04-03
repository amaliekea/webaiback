package org.example.medai.controller;

import org.example.medai.dto.StomachInput;
import org.example.medai.service.MedAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
public class HealthController {
    @Autowired
    private final MedAIService medAIService;

    public HealthController(MedAIService medAIService) {
        this.medAIService = medAIService;
    }
    @Value("${openai.api.key}")
    private String openapikey;

    @GetMapping("/key")
    public String key() {
        return openapikey;
    }

//    @GetMapping("/test")
//    public Map<String, Object> test() {
//        Map<String, Object> testmap = medAIService.promptWebAI();
//        return testmap;
//    }

    @PostMapping("/ask")
    public Map<String, Object> askAboutStomach(@RequestBody StomachInput input) {
        return medAIService.askAboutStomachProblem(input);
    }
}

