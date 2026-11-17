
package com.fiap.minicity.service;

import com.fiap.minicity.dao.CategoriaOcorrenciaDAO;
import com.fiap.minicity.dto.CategoriaOcorrenciaDTO;
import com.fiap.minicity.exception.ConflitoException;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.mapper.CategoriaOcorrenciaMapper;
import com.fiap.minicity.models.CategoriaOcorrencia;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio das categorias de ocorrência.
 * Realiza validações e utiliza o DAO para acessar os dados.
 */
@Service
public class CategoriaOcorrenciaService {

    /** DAO responsável pelo acesso aos dados das categorias de ocorrência. */
    private final CategoriaOcorrenciaDAO dao;

    /**
     * Constrói o serviço recebendo o DAO necessário para acessar o banco de dados.
     *
     * @param dao objeto responsável pelas operações no banco de dados
     */
    public CategoriaOcorrenciaService(CategoriaOcorrenciaDAO dao) {
        this.dao = dao;
    }

    /**
     * Lista todas as categorias de ocorrência cadastradas.
     * Converte os objetos do modelo para DTO antes de retornar.
     *
     * @return lista de categorias de ocorrência em formato DTO
     */
    public List<CategoriaOcorrenciaDTO> listar() {

        return dao.listar()
                .stream()
                .map(CategoriaOcorrenciaMapper::toDTO)
                .toList();
    }

    /**
     * Busca uma categoria de ocorrência pelo seu ID.
     *
     * @param id identificador da categoria que será buscada
     * @return categoria encontrada em formato DTO
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se a categoria não existir
     */
    public CategoriaOcorrenciaDTO buscarPorId(Long id) {

        validarId(id);

        CategoriaOcorrencia categoria = dao.buscarPorId(id);

        if (categoria == null) {
            throw new RecursoNaoEncontradoException(
                    "Categoria de ocorrência não encontrada."
            );
        }

        return CategoriaOcorrenciaMapper.toDTO(categoria);
    }

    /**
     * Cadastra uma nova categoria de ocorrência após validar os dados.
     *
     * @param objeto dados da categoria que será cadastrada
     * @return categoria cadastrada em formato DTO
     * @throws RegraNegocioException se os dados informados forem inválidos
     * @throws ConflitoException se já existir uma categoria com o mesmo nome
     */
    public CategoriaOcorrenciaDTO cadastrar(CategoriaOcorrenciaDTO objeto) {

        validarCategoria(objeto);

        // Verifica se já existe uma categoria com esse nome
        if (dao.existePorNome(objeto.nomeOcorrencia())) {
            throw new ConflitoException(
                    "Já existe uma categoria com esse nome."
            );
        }

        CategoriaOcorrencia categoria = CategoriaOcorrenciaMapper.toModel(objeto);

        CategoriaOcorrencia cadastrada = dao.cadastrar(categoria);

        return CategoriaOcorrenciaMapper.toDTO(cadastrada);
    }

    /**
     * Atualiza os dados de uma categoria de ocorrência existente.
     *
     * @param id identificador da categoria que será atualizada
     * @param objeto novos dados da categoria
     * @return categoria atualizada em formato DTO
     * @throws RegraNegocioException se o ID ou os dados forem inválidos
     * @throws ConflitoException se outra categoria já utilizar o nome informado
     * @throws RecursoNaoEncontradoException se a categoria não existir
     */
    public CategoriaOcorrenciaDTO atualizar(Long id, CategoriaOcorrenciaDTO objeto) {

        validarId(id);
        validarCategoria(objeto);

        // Verifica se outra categoria já utiliza esse nome
        if (dao.existePorNomeEmOutraCategoria(objeto.nomeOcorrencia(), id)) {
            throw new ConflitoException(
                    "Já existe outra categoria com esse nome."
            );
        }

        CategoriaOcorrencia categoria = CategoriaOcorrenciaMapper.toModel(objeto);

        CategoriaOcorrencia atualizada = dao.atualizar(id, categoria);

        if (atualizada == null) {
            throw new RecursoNaoEncontradoException(
                    "Categoria de ocorrência não encontrada."
            );
        }

        return CategoriaOcorrenciaMapper.toDTO(atualizada);
    }

    /**
     * Exclui uma categoria de ocorrência pelo seu ID.
     *
     * @param id identificador da categoria que será excluída
     * @return true se a categoria for excluída com sucesso
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se a categoria não existir
     */
    public boolean deletar(Long id) {

        validarId(id);

        boolean deletada = dao.deletar(id);

        if (!deletada) {
            throw new RecursoNaoEncontradoException(
                    "Categoria de ocorrência não encontrada."
            );
        }

        return true;
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
     * Valida os dados informados para uma categoria de ocorrência.
     *
     * @param objeto dados da categoria que serão validados
     * @throws RegraNegocioException se a categoria não for informada,
     * se o nome estiver vazio ou ultrapassar 80 caracteres,
     * ou se a descrição ultrapassar 300 caracteres
     */
    private void validarCategoria(CategoriaOcorrenciaDTO objeto) {

        if (objeto == null) {
            throw new RegraNegocioException("Categoria não informada.");
        }

        if (objeto.nomeOcorrencia() == null ||
                objeto.nomeOcorrencia().isBlank()) {
            throw new RegraNegocioException(
                    "O nome da categoria é obrigatório."
            );
        }

        if (objeto.nomeOcorrencia().length() > 80) {
            throw new RegraNegocioException(
                    "O nome deve ter no máximo 80 caracteres."
            );
        }

        if (objeto.descTexto() != null &&
                objeto.descTexto().length() > 300) {
            throw new RegraNegocioException(
                    "A descrição deve ter no máximo 300 caracteres."
            );
        }
    }
}
