package com.fiap.minicity.controller;

import com.fiap.minicity.dto.ComentarioDTO;
import com.fiap.minicity.dto.OcorrenciaDTO;
import com.fiap.minicity.mapper.ComentarioMapper;
import com.fiap.minicity.models.Comentario;
import com.fiap.minicity.service.ComentarioService;
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
    private final ComentarioService comentarioService;

    public OcorrenciaController(OcorrenciaService service,
                                ComentarioService comentarioService) {
        this.service = service;
        this.comentarioService = comentarioService;
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

    // ROTAS ANINHADAS (1:N): /ocorrencias/{id}/comentarios

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<ComentarioDTO>> listarComentarios(
            @PathVariable Long id
    ) {
        List<ComentarioDTO> lista = comentarioService.listarPorOcorrencia(id)
                .stream()
                .map(ComentarioMapper::toDTO)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ComentarioDTO> cadastrarComentario(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioDTO dto
    ) {
        Comentario criado = comentarioService.cadastrarNaOcorrencia(
                id, ComentarioMapper.toModel(dto));

        ComentarioDTO resposta = ComentarioMapper.toDTO(criado);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/comentarios/{id}")
                .buildAndExpand(resposta.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(resposta);
    }
}