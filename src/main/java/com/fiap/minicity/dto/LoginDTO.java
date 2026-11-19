
package com.fiap.minicity.dto;

import jakarta.validation.constraints.*;

public record LoginDTO(

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha

) {}
