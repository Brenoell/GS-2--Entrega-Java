package com.fiap.minicity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Representa uma pessoa cadastrada no sistema MiniCity,
 * armazenando seus dados pessoais e informações de acesso.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    /** Identificador único da pessoa no sistema. */
    private Long id;

    /** Nome completo da pessoa cadastrada. */
    private String nome;

    /** Senha utilizada pela pessoa para acessar o sistema. */
    private String senha;

    /** Endereço de e-mail utilizado pela pessoa. */
    private String email;

    /** Perfil de acesso da pessoa no sistema. */
    private String perfil;

    /** Data e horário em que a pessoa foi cadastrada. */
    private LocalDateTime dataCadastro;
}
