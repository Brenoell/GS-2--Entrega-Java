
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

    public ComentarioController(ComentarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ComentarioDTO>> listar() {
        List<ComentarioDTO> comentarios = service.listar()
                .stream()
                .map(ComentarioMapper::toDTO)
                .toList();

        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDTO> buscarPorId(
            @PathVariable Long id
    ) {
        Comentario comentario = service.buscarPorId(id);

        return ResponseEntity.ok(ComentarioMapper.toDTO(comentario));
    }

    @PostMapping
    public ResponseEntity<ComentarioDTO> cadastrar(
            @Valid @RequestBody ComentarioDTO comentarioDTO
    ) {
        Comentario comentario = ComentarioMapper.toModel(comentarioDTO);

        Comentario comentarioCadastrado = service.cadastrar(comentario);

        ComentarioDTO resposta = ComentarioMapper.toDTO(comentarioCadastrado);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resposta.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComentarioDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ComentarioDTO comentarioDTO
    ) {
        Comentario comentario = ComentarioMapper.toModel(comentarioDTO);

        Comentario comentarioAtualizado = service.atualizar(id, comentario);

        return ResponseEntity.ok(ComentarioMapper.toDTO(comentarioAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
