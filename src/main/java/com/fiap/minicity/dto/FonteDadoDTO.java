
package com.fiap.minicity.dto;

import jakarta.validation.constraints.*;

public record FonteDadoDTO(

        Long id,

        @NotBlank(message = "O nome da fonte é obrigatório")
        @Size(min = 3, max = 100, message = "O nome da fonte deve ter entre 3 e 100 caracteres")
        String nomeFonte,

        @NotBlank(message = "O tipo da fonte é obrigatório")
        String tipo,

        @NotBlank(message = "A URL é obrigatória")
        @Size(max = 255, message = "A URL deve ter no máximo 255 caracteres")
        String url

) {
}
