
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.PessoaDTO;
import com.fiap.minicity.service.PessoaService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaService service;

    /**
     * Recebe o PessoaService para acessar
     * as regras de negócio das pessoas.
     */
    public PessoaController(PessoaService service) {
        this.service = service;
    }

    /**
     * Lista todas as pessoas cadastradas.
     */
    @GetMapping
    public ResponseEntity<List<PessoaDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Busca uma pessoa pelo ID informado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Cadastra uma nova pessoa e retorna seus dados
     * junto com o endereço para consultá-la.
     */
    @PostMapping
    public ResponseEntity<PessoaDTO> cadastrar(
            @Valid @RequestBody PessoaDTO pessoaDTO
    ) {
        // Envia os dados recebidos para o Service cadastrar.
        PessoaDTO pessoa = service.cadastrar(pessoaDTO);

        // Monta o endereço da pessoa cadastrada usando seu ID.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pessoa.id())
                .toUri();

        // Retorna HTTP 201 (Created) com os dados cadastrados.
        return ResponseEntity
                .created(location)
                .body(pessoa);
    }

    /**
     * Atualiza os dados de uma pessoa pelo ID informado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PessoaDTO pessoaDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, pessoaDTO));
    }

    /**
     * Exclui uma pessoa pelo ID informado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
