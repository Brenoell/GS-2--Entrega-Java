
package com.fiap.minicity.controller;

import com.fiap.minicity.service.NewsService;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/noticias")
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping("/sao-paulo")
    public ResponseEntity<JsonNode> buscarNoticias() {

        return ResponseEntity.ok(
                service.buscarNoticiasSaoPaulo()
        );
    }
}
