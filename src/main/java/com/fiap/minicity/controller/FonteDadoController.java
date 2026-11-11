
package com.fiap.minicity.controller;

import com.fiap.minicity.models.FonteDado;
import com.fiap.minicity.service.FonteDadoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fontes-dado")
public class FonteDadoController {

    private final FonteDadoService service;

    public FonteDadoController(FonteDadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<FonteDado> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public FonteDado buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public FonteDado cadastrar(@RequestBody FonteDado fonte) {
        return service.cadastrar(fonte);
    }

    @PutMapping("/{id}")
    public FonteDado atualizar(@PathVariable Long id, @RequestBody FonteDado fonte) {
        return service.atualizar(id, fonte);
    }

    @DeleteMapping("/{id}")
    public boolean deletar(@PathVariable Long id) {
        return service.deletar(id);
    }
}
