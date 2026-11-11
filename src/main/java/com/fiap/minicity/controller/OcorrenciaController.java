
package com.fiap.minicity.controller;

import com.fiap.minicity.models.Ocorrencia;
import com.fiap.minicity.service.OcorrenciaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService service;

    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ocorrencia> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Ocorrencia buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Ocorrencia cadastrar(@RequestBody Ocorrencia ocorrencia) {
        return service.cadastrar(ocorrencia);
    }

    @PutMapping("/{id}")
    public Ocorrencia atualizar(@PathVariable Long id, @RequestBody Ocorrencia ocorrencia) {
        return service.atualizar(id, ocorrencia);
    }

    @DeleteMapping("/{id}")
    public boolean deletar(@PathVariable Long id) {
        return service.deletar(id);
    }
}
