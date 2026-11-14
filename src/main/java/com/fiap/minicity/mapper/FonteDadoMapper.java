package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.FonteDadoDTO;
import com.fiap.minicity.models.FonteDado;

public class FonteDadoMapper {

    // Converte Model para DTO
    public static FonteDadoDTO toDTO(FonteDado fonte) {
        if (fonte == null) {
            return null;
        }

        return new FonteDadoDTO(
                fonte.getId(),
                fonte.getNomeFonte(),
                fonte.getTipo(),
                fonte.getUrl()
        );
    }

    // Converte DTO para Model
    public static FonteDado toModel(FonteDadoDTO dto) {
        if (dto == null) {
            return null;
        }

        return new FonteDado(
                dto.id(),
                dto.nomeFonte(),
                dto.tipo(),
                dto.url()
        );
    }
}