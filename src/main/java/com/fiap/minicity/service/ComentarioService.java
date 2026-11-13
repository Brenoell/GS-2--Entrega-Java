
package com.fiap.minicity.service;

import com.fiap.minicity.dao.ComentarioDAO;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.models.Comentario;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioDAO dao;

    public ComentarioService(ComentarioDAO dao) {
        this.dao = dao;
    }

    public List<Comentario> listar() {
        return dao.listar();
    }

    public Comentario buscarPorId(Long id) {

        validarId(id);

        Comentario comentario = dao.buscarPorId(id);

        if (comentario == null) {
            throw new RecursoNaoEncontradoException(
                    "Comentário não encontrado."
            );
        }

        return comentario;
    }

    public Comentario cadastrar(Comentario objeto) {

        validarComentario(objeto);

        return dao.cadastrar(objeto);
    }

    public Comentario atualizar(Long id, Comentario objeto) {

        validarId(id);
        validarComentario(objeto);

        Comentario comentario = dao.atualizar(id, objeto);

        if (comentario == null) {
            throw new RecursoNaoEncontradoException(
                    "Comentário não encontrado."
            );
        }

        return comentario;
    }

    public boolean deletar(Long id) {

        validarId(id);

        boolean deletado = dao.deletar(id);

        if (!deletado) {
            throw new RecursoNaoEncontradoException(
                    "Comentário não encontrado."
            );
        }

        return true;
    }

    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new RegraNegocioException("ID inválido.");
        }
    }

    private void validarComentario(Comentario objeto) {

        if (objeto == null) {
            throw new RegraNegocioException(
                    "Comentário não informado."
            );
        }

        if (objeto.getDescConteudo() == null ||
                objeto.getDescConteudo().isBlank()) {
            throw new RegraNegocioException(
                    "O conteúdo do comentário é obrigatório."
            );
        }

        if (objeto.getDescConteudo().length() > 500) {
            throw new RegraNegocioException(
                    "O comentário deve ter no máximo 500 caracteres."
            );
        }

        if (objeto.getPessoa() == null ||
                objeto.getPessoa().getId() == null ||
                objeto.getPessoa().getId() <= 0) {
            throw new RegraNegocioException(
                    "É necessário informar uma pessoa válida."
            );
        }

        // Distrito e ocorrência são opcionais.
        // Se forem informados, seus IDs precisam ser válidos.

        if (objeto.getDistrito() != null &&
                (objeto.getDistrito().getId() == null ||
                        objeto.getDistrito().getId() <= 0)) {
            throw new RegraNegocioException(
                    "Distrito inválido."
            );
        }

        if (objeto.getOcorrencia() != null &&
                (objeto.getOcorrencia().getId() == null ||
                        objeto.getOcorrencia().getId() <= 0)) {
            throw new RegraNegocioException(
                    "Ocorrência inválida."
            );
        }
    }
}
