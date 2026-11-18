
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.ComentarioDTO;
import com.fiap.minicity.models.Comentario;
import com.fiap.minicity.models.Pessoa;

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

        // Recupera a pessoa recebida no DTO
        if (dto.pessoa() != null) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(dto.pessoa().id());
            c.setPessoa(pessoa);
        }

        // Os outros relacionamentos serão preenchidos pelo Service
        c.setDistrito(null);
        c.setOcorrencia(null);

        return c;
    }
}
