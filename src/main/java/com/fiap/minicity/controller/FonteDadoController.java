
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

    /**
     * Recebe o FonteDadoService para acessar
     * as regras de negócio das fontes de dados.
     */
    public FonteDadoController(FonteDadoService service) {
        this.service = service;
    }

    /**
     * Lista todas as fontes de dados cadastradas.
     */
    @GetMapping
    public ResponseEntity<List<FonteDadoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Busca uma fonte de dados pelo ID informado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FonteDadoDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Cadastra uma nova fonte de dados e retorna seus dados
     * junto com o endereço para consultá-la.
     */
    @PostMapping
    public ResponseEntity<FonteDadoDTO> cadastrar(
            @Valid @RequestBody FonteDadoDTO fonteDTO
    ) {
        // Envia os dados recebidos para a camada Service cadastrar.
        FonteDadoDTO fonte = service.cadastrar(fonteDTO);

        // Monta o endereço da nova fonte de dados usando seu ID.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fonte.id())
                .toUri();

        // Retorna HTTP 201 (Created) com os dados cadastrados.
        return ResponseEntity
                .created(location)
                .body(fonte);
    }

    /**
     * Atualiza os dados de uma fonte de dados com base no ID informado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FonteDadoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FonteDadoDTO fonteDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, fonteDTO));
    }

    /**
     * Exclui uma fonte de dados pelo ID informado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
