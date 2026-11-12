
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.CategoriaOcorrenciaDTO;
import com.fiap.minicity.models.CategoriaOcorrencia;

public class CategoriaOcorrenciaMapper {

    // Converte Model para DTO
    public static CategoriaOcorrenciaDTO toDTO(CategoriaOcorrencia categoria) {

        if (categoria == null) {
            return null;
        }

        return new CategoriaOcorrenciaDTO(
                categoria.getId(),
                categoria.getNomeOcorrencia(),
                categoria.getDescTexto()
        );
    }

    // Converte DTO para Model
    public static CategoriaOcorrencia toModel(CategoriaOcorrenciaDTO categoriaDTO) {

        if (categoriaDTO == null) {
            return null;
        }

        return new CategoriaOcorrencia(
                categoriaDTO.id(),
                categoriaDTO.nomeOcorrencia(),
                categoriaDTO.descTexto()
        );
    }
}
