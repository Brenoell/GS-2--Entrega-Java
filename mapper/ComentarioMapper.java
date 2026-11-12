
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.ComentarioDTO;
import com.fiap.minicity.models.Comentario;

public class ComentarioMapper {

    // Converte Model para DTO
    public static ComentarioDTO toDTO(Comentario comentario) {

        if (comentario == null) {
            return null;
        }

        return new ComentarioDTO(
                comentario.getId(),
                comentario.getDescConteudo(),
                comentario.getDataPublicacao(),

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

        return new Comentario(
                comentarioDTO.id(),
                comentarioDTO.descConteudo(),
                comentarioDTO.dataPublicacao(),

                PessoaMapper.toModel(comentarioDTO.pessoa()),
                DistritoMapper.toModel(comentarioDTO.distrito()),
                OcorrenciaMapper.toModel(comentarioDTO.ocorrencia())
        );
    }
}

