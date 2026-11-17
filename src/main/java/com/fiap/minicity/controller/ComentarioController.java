
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.ComentarioDTO;
import com.fiap.minicity.mapper.ComentarioMapper;
import com.fiap.minicity.models.Comentario;
import com.fiap.minicity.service.ComentarioService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService service;

    /**
     * Recebe o ComentarioService para acessar
     * as regras de negócio dos comentários.
     */
    public ComentarioController(ComentarioService service) {
        this.service = service;
    }

    /**
     * Lista todos os comentários cadastrados e converte
     * cada comentário do Model para DTO antes de retornar.
     */
    @GetMapping
    public ResponseEntity<List<ComentarioDTO>> listar() {
        List<ComentarioDTO> comentarios = service.listar()
                .stream()
                .map(ComentarioMapper::toDTO)
                .toList();

        return ResponseEntity.ok(comentarios);
    }

    /**
     * Busca um comentário pelo ID e converte o resultado
     * para DTO antes de retornar.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDTO> buscarPorId(
            @PathVariable Long id
    ) {
        Comentario comentario = service.buscarPorId(id);

        return ResponseEntity.ok(ComentarioMapper.toDTO(comentario));
    }

    /**
     * Cadastra um novo comentário, convertendo o DTO para Model
     * e o resultado do cadastro novamente para DTO.
     */
    @PostMapping
    public ResponseEntity<ComentarioDTO> cadastrar(
            @Valid @RequestBody ComentarioDTO comentarioDTO
    ) {
        // Converte os dados recebidos em DTO para um objeto Model.
        Comentario comentario = ComentarioMapper.toModel(comentarioDTO);

        // Envia o comentário para a camada Service realizar o cadastro.
        Comentario comentarioCadastrado = service.cadastrar(comentario);

        // Converte o comentário cadastrado para DTO.
        ComentarioDTO resposta = ComentarioMapper.toDTO(comentarioCadastrado);

        // Monta o endereço para consultar o comentário pelo ID.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resposta.id())
                .toUri();

        // Retorna HTTP 201 (Created) com os dados cadastrados.
        return ResponseEntity
                .created(location)
                .body(resposta);
    }

    /**
     * Atualiza um comentário pelo ID, convertendo os dados
     * recebidos em DTO para Model e o resultado para DTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComentarioDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioDTO comentarioDTO
    ) {
        // Converte o DTO recebido para Model.
        Comentario comentario = ComentarioMapper.toModel(comentarioDTO);

        // Envia o ID e os novos dados para a camada Service atualizar.
        Comentario comentarioAtualizado = service.atualizar(id, comentario);

        // Retorna o comentário atualizado convertido para DTO.
        return ResponseEntity.ok(ComentarioMapper.toDTO(comentarioAtualizado));
    }

    /**
     * Exclui um comentário com base no ID informado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
