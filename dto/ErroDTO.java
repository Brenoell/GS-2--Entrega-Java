package com.fiap.minicity.dto;

import java.time.LocalDateTime;

public record ErroDTO(
        int status,
        String erro,
        String mensagem,
        LocalDateTime dataHora
) {}
