
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.CategoriaOcorrenciaDTO;
import com.fiap.minicity.service.CategoriaOcorrenciaService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias-ocorrencia")
public class CategoriaOcorrenciaController {

    private final CategoriaOcorrenciaService service;

    public CategoriaOcorrenciaController(CategoriaOcorrenciaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaOcorrenciaDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaOcorrenciaDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaOcorrenciaDTO> cadastrar(
            @Valid @RequestBody CategoriaOcorrenciaDTO categoriaDTO
    ) {
        CategoriaOcorrenciaDTO categoria = service.cadastrar(categoriaDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoria.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaOcorrenciaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaOcorrenciaDTO categoriaDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, categoriaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
