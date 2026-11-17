
package com.fiap.minicity.service;

import com.fiap.minicity.dao.ComentarioDAO;
import com.fiap.minicity.dao.OcorrenciaDAO;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.models.Comentario;
import com.fiap.minicity.models.Ocorrencia;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio dos comentários.
 * Realiza validações e utiliza os DAOs para acessar os dados.
 */
@Service
public class ComentarioService {

    /** DAO responsável pelas operações de acesso aos comentários. */
    private final ComentarioDAO dao;

    /** DAO responsável pelo acesso aos dados das ocorrências. */
    private final OcorrenciaDAO ocorrenciaDAO;

    /**
     * Constrói o serviço recebendo os DAOs necessários para suas operações.
     *
     * @param dao DAO responsável pelos comentários
     * @param ocorrenciaDAO DAO responsável pelas ocorrências
     */
    public ComentarioService(ComentarioDAO dao, OcorrenciaDAO ocorrenciaDAO) {
        this.dao = dao;
        this.ocorrenciaDAO = ocorrenciaDAO;
    }

    /**
     * Lista todos os comentários cadastrados.
     *
     * @return lista de comentários
     */
    public List<Comentario> listar() {
        return dao.listar();
    }

    /**
     * Busca um comentário pelo seu ID.
     *
     * @param id identificador do comentário que será buscado
     * @return comentário encontrado
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se o comentário não existir
     */
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

    /**
     * Cadastra um novo comentário após validar os dados informados.
     *
     * @param objeto dados do comentário que será cadastrado
     * @return comentário cadastrado
     * @throws RegraNegocioException se os dados do comentário forem inválidos
     */
    public Comentario cadastrar(Comentario objeto) {

        validarComentario(objeto);

        return dao.cadastrar(objeto);
    }

    /**
     * Atualiza os dados de um comentário existente.
     *
     * @param id identificador do comentário que será atualizado
     * @param objeto novos dados do comentário
     * @return comentário atualizado
     * @throws RegraNegocioException se o ID ou os dados forem inválidos
     * @throws RecursoNaoEncontradoException se o comentário não existir
     */
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

    /**
     * Exclui um comentário pelo seu ID.
     *
     * @param id identificador do comentário que será excluído
     * @return true se o comentário for excluído com sucesso
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se o comentário não existir
     */
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

    /**
     * Lista os comentários vinculados a uma ocorrência específica.
     *
     * @param idOcorrencia identificador da ocorrência
     * @return lista de comentários da ocorrência
     * @throws RegraNegocioException se o ID da ocorrência for inválido
     * @throws RecursoNaoEncontradoException se a ocorrência não existir
     */
    public List<Comentario> listarPorOcorrencia(Long idOcorrencia) {

        validarId(idOcorrencia);
        verificarOcorrenciaExiste(idOcorrencia);

        return dao.listarPorOcorrencia(idOcorrencia);
    }

    /**
     * Cadastra um comentário vinculado a uma ocorrência.
     * Define a ocorrência no comentário antes de realizar o cadastro.
     *
     * @param idOcorrencia identificador da ocorrência que receberá o comentário
     * @param comentario dados do comentário que será cadastrado
     * @return comentário cadastrado e vinculado à ocorrência
     * @throws RegraNegocioException se o ID da ocorrência ou os dados do comentário forem inválidos
     * @throws RecursoNaoEncontradoException se a ocorrência não existir
     */
    public Comentario cadastrarNaOcorrencia(Long idOcorrencia, Comentario comentario) {

        validarId(idOcorrencia);
        verificarOcorrenciaExiste(idOcorrencia);

        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setId(idOcorrencia);
        comentario.setOcorrencia(ocorrencia);

        return cadastrar(comentario);
    }

    /**
     * Verifica se uma ocorrência existe no banco de dados.
     *
     * @param idOcorrencia identificador da ocorrência que será verificada
     * @throws RecursoNaoEncontradoException se a ocorrência não existir
     */
    private void verificarOcorrenciaExiste(Long idOcorrencia) {

        if (ocorrenciaDAO.buscarPorId(idOcorrencia) == null) {
            throw new RecursoNaoEncontradoException(
                    "Ocorrência não encontrada."
            );
        }
    }

    /**
     * Verifica se o ID informado é válido.
     *
     * @param id identificador que será validado
     * @throws RegraNegocioException se o ID for nulo ou menor ou igual a zero
     */
    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new RegraNegocioException("ID inválido.");
        }
    }

    /**
     * Valida os dados de um comentário antes do cadastro ou atualização.
     * Verifica o conteúdo, a pessoa e os IDs opcionais de distrito e ocorrência.
     *
     * @param objeto comentário que terá seus dados validados
     * @throws RegraNegocioException se o comentário não for informado,
     * se o conteúdo estiver vazio ou ultrapassar 500 caracteres,
     * se a pessoa não for válida ou se os IDs opcionais forem inválidos
     */
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
        // Se forem informados, seus Ids precisam ser válidos.

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
