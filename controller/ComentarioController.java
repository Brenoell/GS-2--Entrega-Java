
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.ComentarioDTO;
import com.fiap.minicity.mapper.ComentarioMapper;
import com.fiap.minicity.models.Comentario;
import com.fiap.minicity.service.ComentarioService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService service;

    public ComentarioController(ComentarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<ComentarioDTO> listar() {
        return service.listar()
                .stream()
                .map(ComentarioMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ComentarioDTO buscarPorId(@PathVariable Long id) {
        Comentario comentario = service.buscarPorId(id);

        return ComentarioMapper.toDTO(comentario);
    }

    @PostMapping
    public ComentarioDTO cadastrar(@RequestBody ComentarioDTO comentarioDTO) {
        Comentario comentario = ComentarioMapper.toModel(comentarioDTO);

        Comentario comentarioCadastrado = service.cadastrar(comentario);

        return ComentarioMapper.toDTO(comentarioCadastrado);
    }

    @PutMapping("/{id}")
    public ComentarioDTO atualizar(
            @PathVariable Long id,
            @RequestBody ComentarioDTO comentarioDTO
    ) {
        Comentario comentario = ComentarioMapper.toModel(comentarioDTO);

        Comentario comentarioAtualizado = service.atualizar(id, comentario);

        return ComentarioMapper.toDTO(comentarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public boolean deletar(@PathVariable Long id) {
        return service.deletar(id);
    }
}
