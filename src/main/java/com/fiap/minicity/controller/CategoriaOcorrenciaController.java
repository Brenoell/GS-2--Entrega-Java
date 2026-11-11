
package com.fiap.minicity.controller;

import com.fiap.minicity.models.CategoriaOcorrencia;
import com.fiap.minicity.service.CategoriaOcorrenciaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias-ocorrencia")
public class CategoriaOcorrenciaController {

    private final CategoriaOcorrenciaService service;

    public CategoriaOcorrenciaController(CategoriaOcorrenciaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaOcorrencia> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CategoriaOcorrencia buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public CategoriaOcorrencia cadastrar(@RequestBody CategoriaOcorrencia categoria) {
        return service.cadastrar(categoria);
    }

    @PutMapping("/{id}")
    public CategoriaOcorrencia atualizar(@PathVariable Long id, @RequestBody CategoriaOcorrencia categoria) {
        return service.atualizar(id, categoria);
    }

    @DeleteMapping("/{id}")
    public boolean deletar(@PathVariable Long id) {
        return service.deletar(id);
    }
}
