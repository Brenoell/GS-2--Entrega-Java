
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.FonteDadoDTO;
import com.fiap.minicity.models.FonteDado;
import org.modelmapper.ModelMapper;

public class FonteDadoMapper {

    private static final ModelMapper mapper = new ModelMapper();

    // Converte Model para DTO
    public static FonteDadoDTO toDTO(FonteDado fonte) {

        if (fonte == null) {
            return null;
        }

        return mapper.map(fonte, FonteDadoDTO.class);
    }

    // Converte DTO para Model
    public static FonteDado toModel(FonteDadoDTO fonteDTO) {

        if (fonteDTO == null) {
            return null;
        }

        return mapper.map(fonteDTO, FonteDado.class);
    }
}
