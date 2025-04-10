package org.example.examai.service;

import org.example.examai.dto.StudyQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private OpenAIService openAIService;

    @Mock
    private ArticleApiService articleApiService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient mockWebClient;

    private QuizService quizService;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(mockWebClient);

        quizService = new QuizService(openAIService, webClientBuilder);
        ReflectionTestUtils.setField(quizService, "quizapikey", "test-quiz-key");
        ReflectionTestUtils.setField(quizService, "openapikey", "test-openai-key");
        ReflectionTestUtils.setField(quizService, "articleApiService", articleApiService);
    }

    @Test
    void explainTopicWithGPT_includesArticleAndQuiz_whenRequested() {
        // Arrange
        StudyQuestion question = new StudyQuestion();
        question.setTopic("Java");
        question.setLevel("beginner");
        question.setIncludeQuiz(true);

        when(articleApiService.fetchArticle("Java")).thenReturn("Sample article");
        when(openAIService.getResponseFromOpenAI(any(List.class))).thenReturn("Her er en forklaring med quiz og artikel.");

        // Mock WebClient til quiz
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(mockWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(java.util.function.Function.class))).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Quiz 1, Quiz 2"));

        // Act
        String result = quizService.explainTopicWithGPT(question);

        // Assert
        assertTrue(result.contains("forklaring"));
    }
}