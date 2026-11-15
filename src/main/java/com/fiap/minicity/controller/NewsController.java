
package com.fiap.minicity.controller;

import com.fiap.minicity.service.NewsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/noticias")
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping(value = "/sao-paulo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buscarNoticias() {

        String noticias = service.buscarNoticiasSaoPaulo();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(noticias);
    }
}
