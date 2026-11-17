
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

    /**
     * Recebe o DistritoService para acessar
     * as regras de negócio dos distritos.
     */
    public DistritoController(DistritoService service) {
        this.service = service;
    }

    /**
     * Lista todos os distritos cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<DistritoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Busca um distrito pelo ID informado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DistritoDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Cadastra um novo distrito e retorna seus dados
     * junto com o endereço para consultá-lo.
     */
    @PostMapping
    public ResponseEntity<DistritoDTO> cadastrar(
            @Valid @RequestBody DistritoDTO distritoDTO
    ) {
        // Envia os dados recebidos para a camada Service cadastrar.
        DistritoDTO distrito = service.cadastrar(distritoDTO);

        // Monta o endereço da nova categoria usando seu ID.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(distrito.id())
                .toUri();

        // Retorna HTTP 201 (Created) com os dados cadastrados.
        return ResponseEntity
                .created(location)
                .body(distrito);
    }

    /**
     * Atualiza os dados de um distrito com base no ID informado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DistritoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DistritoDTO distritoDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, distritoDTO));
    }

    /**
     * Exclui um distrito pelo ID informado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
