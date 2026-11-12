
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
    public static FonteDado toModel(FonteDadoDTO fonteDTO) {

        if (fonteDTO == null) {
            return null;
        }

        return new FonteDado(
                fonteDTO.id(),
                fonteDTO.nomeFonte(),
                fonteDTO.tipo(),
                fonteDTO.url()
        );
    }
}
