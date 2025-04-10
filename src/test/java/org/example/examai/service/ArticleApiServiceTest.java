package org.example.examai.service;

import org.example.examai.dto.Article;
import org.example.examai.dto.NewsApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleApiServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient mockWebClient;

    private ArticleApiService articleApiService;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(mockWebClient);

        articleApiService = new ArticleApiService(webClientBuilder);

        ReflectionTestUtils.setField(articleApiService, "articleapikey", "test-key");
    }



    @Test
    void fetchArticle() {
        // Arrange
        Article article = new Article();
        article.setTitle("Test Title");
        article.setDescription("Test Description");

        NewsApiResponse fakeResponse = new NewsApiResponse();
        fakeResponse.setArticles(Collections.singletonList(article));

        // Mock WebClient flow
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(mockWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(java.util.function.Function.class))).thenReturn(headersSpec);
        when(headersSpec.header(eq("X-Api-Key"), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(NewsApiResponse.class)).thenReturn(Mono.just(fakeResponse));

        // Act
        String result = articleApiService.fetchArticle("java");

        // Assert
        assertEquals("Test Title: Test Description", result);
    }
}