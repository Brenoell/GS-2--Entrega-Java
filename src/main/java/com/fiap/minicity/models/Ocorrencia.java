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
public class Ocorrencia {

    private Long id;
    private String nomeTitulo;
    private String descOcorrencia;

    //a gravidade é medida de 1 a 10
    private Integer gravidade;
    private String origem;
    private String status;

    private Double latitude;
    private Double longitude;

    private LocalDateTime dataOcorrencia;
    private LocalDateTime dataPublicacao;
    private LocalDateTime dataAtualizacao;

    private Distrito distrito;
    private CategoriaOcorrencia categoria;
    private FonteDado fonte;
    private Pessoa pessoa;

    private String idExterno;
}