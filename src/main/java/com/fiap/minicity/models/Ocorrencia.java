package com.fiap.minicity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Representa uma ocorrência registrada no sistema MiniCity,
 * contendo informações sobre o acontecimento, sua localização,
 * classificação e pessoa responsável pelo registro.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ocorrencia {

    /** Identificador único da ocorrência no sistema. */
    private Long id;

    /** Título que identifica a ocorrência. */
    private String nomeTitulo;

    /** Descrição detalhada do que aconteceu. */
    private String descOcorrencia;

    /** Nível de gravidade da ocorrência, medido de 1 a 5. */
    private Integer gravidade;

    /** Origem da ocorrência, indicando de onde os dados foram obtidos. */
    private String origem;

    /** Situação atual da ocorrência no sistema. */
    private String status;

    /** Latitude da localização onde ocorreu o acontecimento. */
    private Double latitude;

    /** Longitude da localização onde ocorreu o acontecimento. */
    private Double longitude;

    /** Data e horário em que a ocorrência aconteceu. */
    private LocalDateTime dataOcorrencia;

    /** Data e horário em que a ocorrência foi publicada no sistema. */
    private LocalDateTime dataPublicacao;

    /** Data e horário da última atualização da ocorrência. */
    private LocalDateTime dataAtualizacao;

    /** Distrito ao qual a ocorrência está associada. */
    private Distrito distrito;

    /** Categoria utilizada para classificar a ocorrência. */
    private CategoriaOcorrencia categoria;

    /** Fonte responsável por fornecer os dados da ocorrência. */
    private FonteDado fonte;

    /** Pessoa responsável pelo registro da ocorrência. */
    private Pessoa pessoa;

    /** Identificador da ocorrência em um sistema externo, quando existir. */
    private String idExterno;
}
