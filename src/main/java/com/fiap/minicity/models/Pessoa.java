package com.fiap.minicity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    private Long id;
    private String nome;
    private String email;
    private String perfil;
    private LocalDateTime dataCadastro;
}