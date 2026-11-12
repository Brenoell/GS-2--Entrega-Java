
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

    // Converte DTO para Model, incluindo a senha.
    public static Pessoa toModel(PessoaDTO pessoaDTO) {

        if (pessoaDTO == null) {
            return null;
        }

        Pessoa pessoa = new Pessoa();

        pessoa.setId(pessoaDTO.id());
        pessoa.setNome(pessoaDTO.nome());
        pessoa.setSenha(pessoaDTO.senha());
        pessoa.setEmail(pessoaDTO.email());
        pessoa.setPerfil(pessoaDTO.perfil());
        pessoa.setDataCadastro(pessoaDTO.dataCadastro());

        return pessoa;
    }
}
