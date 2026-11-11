package com.fiap.minicity.controller;

import com.fiap.minicity.models.Distrito;
import com.fiap.minicity.service.DistritoService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/distritos")
public class DistritoController {

    private final DistritoService service;

    public DistritoController(DistritoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Distrito> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Distrito buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Distrito cadastrar(@RequestBody Distrito distrito) {
        return service.cadastrar(distrito);
    }

    @PutMapping("/{id}")
    public Distrito atualizar(@PathVariable Long id, @RequestBody Distrito distrito) {
        return service.atualizar(id, distrito);
    }

    @DeleteMapping("/{id}")
    public boolean deletar(@PathVariable Long id) {
        return service.deletar(id);
    }
}