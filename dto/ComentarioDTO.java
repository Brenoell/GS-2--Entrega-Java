
package com.fiap.minicity.dto;

import java.time.LocalDateTime;

public record ComentarioDTO(
        Long id,
        String descConteudo,
        LocalDateTime dataPublicacao,
        PessoaDTO pessoa,
        DistritoDTO distrito,
        OcorrenciaDTO ocorrencia
) {
}
