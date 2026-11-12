
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.DistritoDTO;
import com.fiap.minicity.models.Distrito;

public class DistritoMapper {

    // Converte Model para DTO
    public static DistritoDTO toDTO(Distrito distrito) {

        if (distrito == null) {
            return null;
        }

        return new DistritoDTO(
                distrito.getId(),
                distrito.getNomeDistrito(),
                distrito.getZona(),
                distrito.getLatitude(),
                distrito.getLongitude()
        );
    }

    // Converte DTO para Model
    public static Distrito toModel(DistritoDTO distritoDTO) {

        if (distritoDTO == null) {
            return null;
        }

        return new Distrito(
                distritoDTO.id(),
                distritoDTO.nomeDistrito(),
                distritoDTO.zona(),
                distritoDTO.latitude(),
                distritoDTO.longitude()
        );
    }
}
