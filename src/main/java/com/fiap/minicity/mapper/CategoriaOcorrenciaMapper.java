
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.CategoriaOcorrenciaDTO;
import com.fiap.minicity.models.CategoriaOcorrencia;
import org.modelmapper.ModelMapper;

public class CategoriaOcorrenciaMapper {

    private static final ModelMapper mapper = new ModelMapper();

    // Converte Model para DTO
    public static CategoriaOcorrenciaDTO toDTO(CategoriaOcorrencia categoria) {

        if (categoria == null) {
            return null;
        }

        return mapper.map(categoria, CategoriaOcorrenciaDTO.class);
    }

    // Converte DTO para Model
    public static CategoriaOcorrencia toModel(CategoriaOcorrenciaDTO categoriaDTO) {

        if (categoriaDTO == null) {
            return null;
        }

        return mapper.map(categoriaDTO, CategoriaOcorrencia.class);
    }
}
