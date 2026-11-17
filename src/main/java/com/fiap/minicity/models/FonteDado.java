package com.fiap.minicity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa uma fonte de dados utilizada pelo sistema MiniCity
 * para identificar a origem das informações das ocorrências.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FonteDado {

    /** Identificador único da fonte de dados. */
    private Long id;

    /** Nome que identifica a fonte de dados. */
    private String nomeFonte;

    /** Tipo ou classificação da fonte de dados. */
    private String tipo;

    /** Endereço da página ou serviço de onde os dados são obtidos. */
    private String url;
}
