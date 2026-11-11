
package com.fiap.minicity.controller;

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
    public List<Comentario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Comentario buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Comentario cadastrar(@RequestBody Comentario comentario) {
        return service.cadastrar(comentario);
    }

    @PutMapping("/{id}")
    public Comentario atualizar(@PathVariable Long id, @RequestBody Comentario comentario) {
        return service.atualizar(id, comentario);
    }

    @DeleteMapping("/{id}")
    public boolean deletar(@PathVariable Long id) {
        return service.deletar(id);
    }
}
