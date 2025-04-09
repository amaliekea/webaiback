package org.example.examai.service;

import org.example.examai.dto.Article;
import org.example.examai.dto.NewsApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ArticleApiService {

    private final WebClient articleApiWebClient;

    @Autowired
    public ArticleApiService(WebClient.Builder webClientBuilder) {
        this.articleApiWebClient = webClientBuilder
                .baseUrl("https://newsapi.org/v2").build();
    }

    @Value("${ARTICLEAPIKEY}")
    private String articleapikey;

    public String fetchArticle(String topic) {
        NewsApiResponse response = articleApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/everything")
                        .queryParam("q", topic)
                        .queryParam("language", "en")
                        .queryParam("sortBy", "relevancy")
                        .queryParam("pageSize", 3)
                        .build())
                .header("X-Api-Key", articleapikey)
                .retrieve()
                .bodyToMono(NewsApiResponse.class)
                .block();

        if (response != null && response.getArticles() != null && !response.getArticles().isEmpty()) {
            Article firstArticle = response.getArticles().get(0);
            return firstArticle.getTitle() + ": " + firstArticle.getDescription();
        }

        return "No relevant article found.";
    }
}
