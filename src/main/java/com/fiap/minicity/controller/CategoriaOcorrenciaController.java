
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

    /**
     * Recebe o CategoriaOcorrenciaService para acessar
     * as regras de negócio das categorias de ocorrência.
     */
    public CategoriaOcorrenciaController(CategoriaOcorrenciaService service) {
        this.service = service;
    }

    /**
     * Lista todas as categorias de ocorrência cadastradas.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaOcorrenciaDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Busca uma categoria de ocorrência pelo seu ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaOcorrenciaDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Cadastra uma nova categoria de ocorrência e retorna
     * os dados cadastrados com o endereço para consultá-la.
     */
    @PostMapping
    public ResponseEntity<CategoriaOcorrenciaDTO> cadastrar(
            @Valid @RequestBody CategoriaOcorrenciaDTO categoriaDTO
    ) {
        CategoriaOcorrenciaDTO categoria = service.cadastrar(categoriaDTO);

        // Monta o endereço da nova categoria usando seu ID.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoria.id())
                .toUri();

        // Retorna HTTP 201 (Created) com os dados cadastrados.
        return ResponseEntity
                .created(location)
                .body(categoria);
    }

    /**
     * Atualiza os dados de uma categoria de ocorrência
     * com base no ID informado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaOcorrenciaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaOcorrenciaDTO categoriaDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, categoriaDTO));
    }

    /**
     * Exclui uma categoria de ocorrência pelo seu ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
