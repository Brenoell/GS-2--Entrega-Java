
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.DistritoDTO;
import com.fiap.minicity.service.DistritoService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody DistritoDTO distritoDTO
    ) {
        DistritoDTO distrito = service.cadastrar(distritoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(distrito);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistritoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody DistritoDTO distritoDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, distritoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
