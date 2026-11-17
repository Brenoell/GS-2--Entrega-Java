
package com.fiap.minicity.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CriarOcorrenciaDTO(

        @NotBlank(message = "O título da ocorrência é obrigatório")
        @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres")
        String nomeTitulo,

        @NotBlank(message = "A descrição da ocorrência é obrigatória")
        @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
        String descOcorrencia,

        @NotNull(message = "A gravidade é obrigatória")
        @Min(value = 1, message = "A gravidade mínima é 1")
        @Max(value = 5, message = "A gravidade máxima é 5")
        Integer gravidade,

        @NotNull(message = "A latitude é obrigatória")
        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        Double latitude,

        @NotNull(message = "A longitude é obrigatória")
        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        Double longitude,

        @NotNull(message = "A data da ocorrência é obrigatória")
        @PastOrPresent(message = "A data da ocorrência não pode ser futura")
        LocalDateTime dataOcorrencia,

        @NotNull(message = "O distrito é obrigatório")
        DistritoDTO distrito,

        @NotNull(message = "A categoria é obrigatória")
        CategoriaOcorrenciaDTO categoria

) {
}
