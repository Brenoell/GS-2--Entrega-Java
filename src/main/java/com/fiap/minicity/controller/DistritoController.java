
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.DistritoDTO;
import com.fiap.minicity.service.DistritoService;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/distritos")
public class DistritoController {

    private final DistritoService service;

    public DistritoController(DistritoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DistritoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistritoDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DistritoDTO> cadastrar(
            @Valid @RequestBody DistritoDTO distritoDTO
    ) {
        DistritoDTO distrito = service.cadastrar(distritoDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(distrito.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(distrito);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistritoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DistritoDTO distritoDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, distritoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
