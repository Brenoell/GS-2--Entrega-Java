
package com.fiap.minicity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Serviço responsável por buscar notícias relacionadas à cidade de São Paulo.
 * Utiliza a API externa NewsAPI para obter as notícias mais recentes.
 */
@Service
public class NewsService {

    /** Cliente utilizado para realizar requisições HTTP à NewsAPI. */
    private final RestClient restClient;

    /**
     * Constrói o serviço configurando o cliente de comunicação com a NewsAPI.
     * Define a URL base e a chave de autenticação da API.
     *
     * @param apiKey chave de autenticação utilizada para acessar a NewsAPI,
     *               obtida da configuração NEWS_API_KEY
     */
    public NewsService(@Value("${NEWS_API_KEY}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl("https://newsapi.org/v2")
                .defaultHeader("X-Api-Key", apiKey)
                .build();
    }

    /**
     * Busca as notícias mais recentes relacionadas à cidade de São Paulo.
     * Filtra os resultados em português, pesquisando nos títulos e descrições,
     * e ordena as notícias pela data de publicação.
     *
     * @return resposta da NewsAPI em formato String, contendo os dados das notícias
     */
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
