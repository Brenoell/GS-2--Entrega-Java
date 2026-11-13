
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.DistritoDTO;
import com.fiap.minicity.models.Distrito;
import org.modelmapper.ModelMapper;

public class DistritoMapper {

    private static final ModelMapper mapper = new ModelMapper();

    // Converte Model para DTO
    public static DistritoDTO toDTO(Distrito distrito) {

        if (distrito == null) {
            return null;
        }

        return mapper.map(distrito, DistritoDTO.class);
    }

    // Converte DTO para Model
    public static Distrito toModel(DistritoDTO distritoDTO) {

        if (distritoDTO == null) {
            return null;
        }

        return mapper.map(distritoDTO, Distrito.class);
    }
}
