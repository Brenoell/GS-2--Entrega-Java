
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.ComentarioDTO;
import com.fiap.minicity.models.Comentario;
import org.modelmapper.ModelMapper;

public class ComentarioMapper {

    private static final ModelMapper mapper = new ModelMapper();

    // Converte Model para DTO
    public static ComentarioDTO toDTO(Comentario comentario) {

        if (comentario == null) {
            return null;
        }

        ComentarioDTO dto = mapper.map(comentario, ComentarioDTO.class);

        return new ComentarioDTO(
                dto.id(),
                dto.descConteudo(),
                dto.dataPublicacao(),

                PessoaMapper.toDTO(comentario.getPessoa()),
                DistritoMapper.toDTO(comentario.getDistrito()),
                OcorrenciaMapper.toDTO(comentario.getOcorrencia())
        );
    }

    // Converte DTO para Model
    public static Comentario toModel(ComentarioDTO comentarioDTO) {

        if (comentarioDTO == null) {
            return null;
        }

        Comentario comentario = mapper.map(comentarioDTO, Comentario.class);

        // Os relacionamentos serão preenchidos pelo Service
        comentario.setPessoa(null);
        comentario.setDistrito(null);
        comentario.setOcorrencia(null);

        return comentario;
    }
}
