
package com.fiap.minicity.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record PessoaDTO(

        Long id,

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "O perfil é obrigatório")
        String perfil,

        LocalDateTime dataCadastro

) {
}
