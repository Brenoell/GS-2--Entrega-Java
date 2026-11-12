
package com.fiap.minicity.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record ComentarioDTO(

        Long id,

        @NotBlank(message = "O conteúdo do comentário é obrigatório")
        @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
        String descConteudo,

        LocalDateTime dataPublicacao,

        @NotNull(message = "A pessoa é obrigatória")
        @Valid
        PessoaDTO pessoa,

        DistritoDTO distrito,

        OcorrenciaDTO ocorrencia

) {
}
