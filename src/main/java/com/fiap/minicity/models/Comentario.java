package com.fiap.minicity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Representa um comentário publicado no sistema MiniCity,
 * armazenando seu conteúdo, data de publicação e relacionamentos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {

    /** Identificador único do comentário no sistema. */
    private Long id;

    /** Texto com o conteúdo escrito no comentário. */
    private String descConteudo;

    /** Data e horário em que o comentário foi publicado. */
    private LocalDateTime dataPublicacao;

    /** Pessoa responsável por escrever o comentário. */
    private Pessoa pessoa;

    /** Distrito ao qual o comentário está relacionado. */
    private Distrito distrito;

    /** Ocorrência na qual o comentário foi publicado. */
    private Ocorrencia ocorrencia;
}
