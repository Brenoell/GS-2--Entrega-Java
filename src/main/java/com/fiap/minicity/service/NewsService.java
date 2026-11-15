
package com.fiap.minicity.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NewsService {

    private final RestClient restClient;

    public NewsService(@Value("${newsapi.key}") String apiKey) {

        this.restClient = RestClient.builder()
                .baseUrl("https://newsapi.org/v2")
                .defaultHeader("X-Api-Key", apiKey)
                .build();
    }

    public JsonNode buscarNoticiasSaoPaulo() {

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

        // Exibe a resposta original da NewsAPI no console
        System.out.println("RESPOSTA DA NEWSAPI:");
        System.out.println(resposta);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readTree(resposta);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao converter notícias para JSON", e
            );
        }
    }
}
