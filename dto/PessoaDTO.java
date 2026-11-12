
package com.fiap.minicity.dto;

import java.time.LocalDateTime;

public record PessoaDTO(
        Long id,
        String nome,
        String senha,
        String email,
        String perfil,
        LocalDateTime dataCadastro
) {
}
