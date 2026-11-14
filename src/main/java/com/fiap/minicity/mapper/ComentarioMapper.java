package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.ComentarioDTO;
import com.fiap.minicity.models.Comentario;

public class ComentarioMapper {

    // Converte Model para DTO
    public static ComentarioDTO toDTO(Comentario c) {
        if (c == null) {
            return null;
        }

        return new ComentarioDTO(
                c.getId(),
                c.getDescConteudo(),
                c.getDataPublicacao(),

                PessoaMapper.toDTO(c.getPessoa()),
                DistritoMapper.toDTO(c.getDistrito()),
                OcorrenciaMapper.toDTO(c.getOcorrencia())
        );
    }

    // Converte DTO para Model
    public static Comentario toModel(ComentarioDTO dto) {
        if (dto == null) {
            return null;
        }

        Comentario c = new Comentario();
        c.setId(dto.id());
        c.setDescConteudo(dto.descConteudo());
        c.setDataPublicacao(dto.dataPublicacao());

        // Os relacionamentos serão preenchidos pelo Service
        c.setPessoa(null);
        c.setDistrito(null);
        c.setOcorrencia(null);

        return c;
    }
}