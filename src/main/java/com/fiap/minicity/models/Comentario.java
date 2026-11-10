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
public class Comentario {

    private Long id;
    private String descConteudo;
    private LocalDateTime dataPublicacao;

    private Pessoa pessoa;
    private Distrito distrito;
    private Ocorrencia ocorrencia;
}