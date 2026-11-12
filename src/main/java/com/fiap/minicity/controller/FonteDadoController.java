
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.FonteDadoDTO;
import com.fiap.minicity.service.FonteDadoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<FonteDadoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FonteDadoDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FonteDadoDTO> cadastrar(
            @RequestBody FonteDadoDTO fonteDTO
    ) {
        FonteDadoDTO fonte = service.cadastrar(fonteDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(fonte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FonteDadoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody FonteDadoDTO fonteDTO
    ) {
        return ResponseEntity.ok(service.atualizar(id, fonteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
