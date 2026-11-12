
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.FonteDadoDTO;
import com.fiap.minicity.service.FonteDadoService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/fontes-dado")
public class FonteDadoController {

    private final FonteDadoService service;

    public FonteDadoController(FonteDadoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FonteDadoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FonteDadoDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FonteDadoDTO> cadastrar(
            @Valid @RequestBody FonteDadoDTO fonteDTO
    ) {
        FonteDadoDTO fonte = service.cadastrar(fonteDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fonte.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(fonte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FonteDadoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FonteDadoDTO fonteDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, fonteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
