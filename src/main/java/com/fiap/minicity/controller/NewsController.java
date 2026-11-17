
package com.fiap.minicity.controller;

import com.fiap.minicity.service.NewsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/noticias")
public class NewsController {

    private final NewsService service;

    /**
     * Recebe o NewsService para acessar
     * as regras de busca de notícias.
     */
    public NewsController(NewsService service) {
        this.service = service;
    }

    /**
     * Busca notícias relacionadas a São Paulo
     * e retorna o resultado no formato JSON.
     */
    @GetMapping(value = "/sao-paulo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buscarNoticias() {

        // Solicita ao Service as notícias de São Paulo.
        String noticias = service.buscarNoticiasSaoPaulo();

        // Retorna HTTP 200 (OK) com as notícias em JSON.
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(noticias);
    }
}
