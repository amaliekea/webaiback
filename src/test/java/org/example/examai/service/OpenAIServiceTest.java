package org.example.examai.service;

import org.example.examai.dto.Message;
import org.example.examai.dto.RequestDTO;
import org.example.examai.dto.ResponseDTO;
import org.example.examai.dto.Choice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenAIServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient mockWebClient;

    private OpenAIService openAIService;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(mockWebClient);

        openAIService = new OpenAIService(webClientBuilder);
        ReflectionTestUtils.setField(openAIService, "openapikey", "test-openai-key");
    }

    @Test
    void getResponseFromOpenAI() {
        // Arrange
        Message inputMessage = new Message("user", "Hvad er Java?");
        Message responseMessage = new Message("assistant", "Java er et programmeringssprog.");

        Choice choice = new Choice();
        choice.setMessage(responseMessage);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setChoices(List.of(choice));

        // Mocks for WebClient flow
        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(mockWebClient.post()).thenReturn(uriSpec);
        when(uriSpec.contentType(any())).thenReturn(bodySpec);
        when(bodySpec.headers(any())).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any(RequestDTO.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ResponseDTO.class)).thenReturn(Mono.just(responseDTO));

        // Act
        String result = openAIService.getResponseFromOpenAI(List.of(inputMessage));

        // Assert
        assertEquals("Java er et programmeringssprog.", result);
    }

}