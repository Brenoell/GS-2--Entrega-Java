
package com.fiap.minicity.dto;

import jakarta.validation.constraints.*;

public record DistritoDTO(

        Long id,

        @NotBlank(message = "O nome do distrito é obrigatório")
        @Size(min = 2, max = 100, message = "O nome do distrito deve ter entre 2 e 100 caracteres")
        String nomeDistrito,

        @NotBlank(message = "A zona é obrigatória")
        String zona,

        @DecimalMin(value = "-90.0", message = "A latitude mínima é -90")
        @DecimalMax(value = "90.0", message = "A latitude máxima é 90")
        double latitude,

        @DecimalMin(value = "-180.0", message = "A longitude mínima é -180")
        @DecimalMax(value = "180.0", message = "A longitude máxima é 180")
        double longitude

) {
}
