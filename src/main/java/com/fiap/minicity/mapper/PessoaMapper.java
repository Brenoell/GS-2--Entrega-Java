
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.PessoaDTO;
import com.fiap.minicity.models.Pessoa;
import org.modelmapper.ModelMapper;

public class PessoaMapper {

    private static final ModelMapper mapper = new ModelMapper();

    // Converte Model para DTO, sem expor a senha.
    public static PessoaDTO toDTO(Pessoa pessoa) {

        if (pessoa == null) {
            return null;
        }

        PessoaDTO dto = mapper.map(pessoa, PessoaDTO.class);

        return new PessoaDTO(
                dto.id(),
                dto.nome(),
                null,
                dto.email(),
                dto.perfil(),
                dto.dataCadastro()
        );
    }

    // Converte DTO para Model.
    public static Pessoa toModel(PessoaDTO pessoaDTO) {

        if (pessoaDTO == null) {
            return null;
        }

        return mapper.map(pessoaDTO, Pessoa.class);
    }
}
