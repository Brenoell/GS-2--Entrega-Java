
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.ComentarioDTO;
import com.fiap.minicity.dto.OcorrenciaDTO;
import com.fiap.minicity.dto.CriarOcorrenciaDTO;

import com.fiap.minicity.exception.CredenciaisInvalidasException;

import com.fiap.minicity.mapper.ComentarioMapper;
import com.fiap.minicity.models.Comentario;

import com.fiap.minicity.service.ComentarioService;
import com.fiap.minicity.service.OcorrenciaService;

import jakarta.servlet.http.HttpSession;
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
    private final ComentarioService comentarioService;

    /**
     * Recebe os Services necessários para gerenciar
     * ocorrências e comentários.
     */
    public OcorrenciaController(
            OcorrenciaService service,
            ComentarioService comentarioService
    ) {
        this.service = service;
        this.comentarioService = comentarioService;
    }

    /**
     * Lista todas as ocorrências cadastradas.
     */
    @GetMapping
    public ResponseEntity<List<OcorrenciaDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    /**
     * Busca uma ocorrência pelo ID informado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Cadastra uma ocorrência vinculada à pessoa autenticada
     * e retorna os dados da ocorrência criada.
     */
    @PostMapping
    public ResponseEntity<OcorrenciaDTO> cadastrar(
            @Valid @RequestBody CriarOcorrenciaDTO ocorrenciaDTO,
            HttpSession session
    ) {

        // Recupera o ID da pessoa que fez login.
        Long idPessoa = (Long) session.getAttribute("idPessoa");

        // Impede o cadastro caso não exista uma pessoa autenticada.
        if (idPessoa == null) {
            throw new CredenciaisInvalidasException(
                    "Usuário não autenticado."
            );
        }

        // Cadastra a ocorrência vinculada à pessoa logada.
        OcorrenciaDTO ocorrencia =
                service.cadastrarComoCidadao(ocorrenciaDTO, idPessoa);

        // Monta o endereço para consultar a ocorrência criada.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ocorrencia.id())
                .toUri();

        // Retorna HTTP 201 (Created) com os dados cadastrados.
        return ResponseEntity
                .created(location)
                .body(ocorrencia);
    }

    /**
     * Atualiza os dados de uma ocorrência pelo ID informado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OcorrenciaDTO ocorrenciaDTO
    ) {
        return ResponseEntity.ok(
                service.atualizar(id, ocorrenciaDTO)
        );
    }

    /**
     * Exclui uma ocorrência pelo ID informado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Lista todos os comentários associados a uma ocorrência.
     */
    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<ComentarioDTO>> listarComentarios(
            @PathVariable Long id
    ) {
        List<ComentarioDTO> lista =
                comentarioService.listarPorOcorrencia(id)
                        .stream()
                        .map(ComentarioMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(lista);
    }

    /**
     * Cadastra um comentário em uma ocorrência específica
     * e retorna os dados do comentário criado.
     */
    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ComentarioDTO> cadastrarComentario(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioDTO dto
    ) {
        // Converte o DTO recebido em um objeto Comentario e cadastra.
        Comentario criado =
                comentarioService.cadastrarNaOcorrencia(
                        id,
                        ComentarioMapper.toModel(dto)
                );

        // Converte o comentário criado para DTO.
        ComentarioDTO resposta = ComentarioMapper.toDTO(criado);

        // Monta o endereço para consultar o comentário criado.
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/comentarios/{id}")
                .buildAndExpand(resposta.id())
                .toUri();

        // Retorna HTTP 201 (Created) com os dados do comentário.
        return ResponseEntity
                .created(location)
                .body(resposta);
    }
}
