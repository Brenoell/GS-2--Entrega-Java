
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.OcorrenciaDTO;
import com.fiap.minicity.service.OcorrenciaService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService service;

    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OcorrenciaDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<OcorrenciaDTO> cadastrar(
            @Valid @RequestBody OcorrenciaDTO ocorrenciaDTO
    ) {
        OcorrenciaDTO ocorrencia = service.cadastrar(ocorrenciaDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ocorrencia.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(ocorrencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OcorrenciaDTO ocorrenciaDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, ocorrenciaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
