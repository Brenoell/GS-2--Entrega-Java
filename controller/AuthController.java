
package com.fiap.minicity.controller;

import com.fiap.minicity.dto.LoginDTO;
import com.fiap.minicity.dto.PessoaDTO;
import com.fiap.minicity.exception.CredenciaisInvalidasException;
import com.fiap.minicity.service.PessoaService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PessoaService service;

    public AuthController(PessoaService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<PessoaDTO> login(
            @RequestBody LoginDTO loginDTO,
            HttpSession session
    ) {
        PessoaDTO pessoa = service.login(loginDTO);

        session.setAttribute("idPessoa", pessoa.id());

        return ResponseEntity.ok(pessoa);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sessao")
    public ResponseEntity<Long> verificarSessao(HttpSession session) {

        Long idPessoa = (Long) session.getAttribute("idPessoa");

        if (idPessoa == null) {
            throw new CredenciaisInvalidasException(
                    "Usuário não autenticado."
            );
        }

        return ResponseEntity.ok(idPessoa);
    }
}
