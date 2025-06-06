package org.example.examai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.examai.dto.StudyQuestion;
import org.example.examai.service.QuizService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@TestPropertySource(properties = "OPENAPIKEY=test1234")
@WebMvcTest(QuizController.class)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuizService quizService;


    @Test
    void key() throws Exception {
        mockMvc.perform(get("/key"))
                .andExpect(status().isOk());
    }

    @Test
    void studyHelper() throws Exception {
        StudyQuestion question = new StudyQuestion();
        question.setTopic("Java");
        question.setLevel("medium");
        question.setIncludeQuiz(true);

        //  "dummy" returnværdi
        when(quizService.explainTopicWithGPT(any(StudyQuestion.class))).thenReturn("Java is a...");

        mockMvc.perform(post("/study-helper")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk())
                .andExpect(content().string("Java is a..."));
    }
}