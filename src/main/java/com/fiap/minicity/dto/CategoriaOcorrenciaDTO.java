
package com.fiap.minicity.dto;

import jakarta.validation.constraints.*;

public record CategoriaOcorrenciaDTO(

        Long id,

        @NotBlank(message = "O nome da categoria é obrigatório")
        @Size(min = 3, max = 100, message = "O nome da categoria deve ter entre 3 e 100 caracteres")
        String nomeOcorrencia,

        @NotBlank(message = "A descrição da categoria é obrigatória")
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String descTexto

) {
}
