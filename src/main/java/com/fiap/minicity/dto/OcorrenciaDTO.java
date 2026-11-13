
package com.fiap.minicity.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record OcorrenciaDTO(

        Long id,

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

        @NotBlank(message = "A origem é obrigatória")
        String origem,

        @NotBlank(message = "O status é obrigatório")
        String status,

        @NotNull(message = "A latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "A latitude mínima é -90")
        @DecimalMax(value = "90.0", message = "A latitude máxima é 90")
        Double latitude,

        @NotNull(message = "A longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "A longitude mínima é -180")
        @DecimalMax(value = "180.0", message = "A longitude máxima é 180")
        Double longitude,

        @NotNull(message = "A data da ocorrência é obrigatória")
        @PastOrPresent(message = "A data da ocorrência não pode ser futura")
        LocalDateTime dataOcorrencia,

        LocalDateTime dataPublicacao,

        LocalDateTime dataAtualizacao,

        @NotNull(message = "O distrito é obrigatório")
        DistritoDTO distrito,

        @NotNull(message = "A categoria é obrigatória")
        CategoriaOcorrenciaDTO categoria,

        FonteDadoDTO fonte,

        PessoaDTO pessoa,

        @Size(max = 100, message = "O ID externo deve ter no máximo 100 caracteres")
        String idExterno

) {
}
