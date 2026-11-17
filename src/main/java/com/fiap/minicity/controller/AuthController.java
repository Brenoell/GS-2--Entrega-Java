
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

    /**
     * Construtor que recebe o PessoaService para utilizar
     * as regras de negócio relacionadas à pessoa.
     */
    public AuthController(PessoaService service) {
        this.service = service;
    }

    /**
     * Realiza o login, verifica as credenciais e armazena
     * o ID da pessoa autenticada na sessão HTTP.
     */
    @PostMapping("/login")
    public ResponseEntity<PessoaDTO> login(
            @RequestBody LoginDTO loginDTO,
            HttpSession session
    ) {
        PessoaDTO pessoa = service.login(loginDTO);

        session.setAttribute("idPessoa", pessoa.id());

        return ResponseEntity.ok(pessoa);
    }

    /**
     * Realiza o logout e encerra a sessão HTTP do usuário.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();

        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica se existe uma pessoa autenticada na sessão
     * e retorna seu ID. Caso não exista, lança uma exceção.
     */
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
