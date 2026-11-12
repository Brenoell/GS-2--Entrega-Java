package com.fiap.minicity.dto;

import java.time.LocalDateTime;

public record OcorrenciaDTO(
        Long id,
        String nomeTitulo,
        String descOcorrencia,
        Integer gravidade,
        String origem,
        String status,
        Double latitude,
        Double longitude,
        LocalDateTime dataOcorrencia,
        LocalDateTime dataPublicacao,
        LocalDateTime dataAtualizacao,
        DistritoDTO distrito,
        CategoriaOcorrenciaDTO categoria,
        FonteDadoDTO fonte,
        PessoaDTO pessoa,
        String idExterno
) {
}
