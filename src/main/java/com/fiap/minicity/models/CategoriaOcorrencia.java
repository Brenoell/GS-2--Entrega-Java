
package com.fiap.minicity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa uma categoria utilizada para classificar
 * as ocorrências registradas no sistema MiniCity.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaOcorrencia {

    /** Identificador único da categoria no sistema. */
    private Long id;

    /** Nome que identifica a categoria da ocorrência. */
    private String nomeOcorrencia;

    /** Descrição com informações adicionais sobre a categoria. */
    private String descTexto;
}
