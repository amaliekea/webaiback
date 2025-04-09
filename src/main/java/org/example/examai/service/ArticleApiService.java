package org.example.examai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ArticleApiService {

    private final WebClient articleApiWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper(); // til at parse JSON

    public ArticleApiService(WebClient.Builder webClientBuilder) {
        this.articleApiWebClient = webClientBuilder
                .baseUrl("https://newsapi.org/v2")
                .build();
    }

    @Value("${ARTICLEAPIKEY}")
    private String articleapikey;

    public String fetchArticle(String topic) {
        String jsonResponse = articleApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/everything")
                        .queryParam("q", topic)
                        .queryParam("language", "en")
                        .queryParam("sortBy", "relevancy")
                        .queryParam("pageSize", 3)
                        .build())
                .header("X-Api-Key", articleapikey)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse); // konverter JSON-strengen til et objekt
            JsonNode articles = root.path("articles");

            if (articles.isArray() && articles.size() > 0) {
                JsonNode firstArticle = articles.get(0);
                String title = firstArticle.path("title").asText();
                String description = firstArticle.path("description").asText();
                return title + ": " + description;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "No relevant article found.";
    }
}
