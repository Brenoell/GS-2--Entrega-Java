
package com.fiap.minicity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NewsService {

    private final RestClient restClient;

    public NewsService(@Value("${NEWS_API_KEY}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("https://newsapi.org/v2")
                .defaultHeader("X-Api-Key", apiKey)
                .build();
    }

    public String buscarNoticiasSaoPaulo() {

        String resposta = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/everything")
                        .queryParam("q",
                                "(\"Prefeitura de São Paulo\" OR " +
                                        "\"Câmara Municipal de São Paulo\" OR " +
                                        "\"cidade de São Paulo\" OR " +
                                        "\"município de São Paulo\" OR " +
                                        "\"capital paulista\")"
                        )
                        .queryParam("searchIn", "title,description")
                        .queryParam("language", "pt")
                        .queryParam("sortBy", "publishedAt")
                        .queryParam("pageSize", 10)
                        .build()
                )
                .retrieve()
                .body(String.class);

        return resposta;
    }
}
