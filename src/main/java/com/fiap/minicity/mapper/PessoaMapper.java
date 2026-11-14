package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.PessoaDTO;
import com.fiap.minicity.models.Pessoa;

public class PessoaMapper {

    // Converte Model para DTO, sem expor a senha.
    public static PessoaDTO toDTO(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        return new PessoaDTO(
                pessoa.getId(),
                pessoa.getNome(),
                null,
                pessoa.getEmail(),
                pessoa.getPerfil(),
                pessoa.getDataCadastro()
        );
    }

    // Converte DTO para Model.
    public static Pessoa toModel(PessoaDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Pessoa(
                dto.id(),
                dto.nome(),
                dto.senha(),
                dto.email(),
                dto.perfil(),
                dto.dataCadastro()
        );
    }
}