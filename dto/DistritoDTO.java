package com.fiap.minicity.dto;

public record DistritoDTO(
        Long id,
        String nomeDistrito,
        String zona,
        double latitude,
        double longitude
) {
}
