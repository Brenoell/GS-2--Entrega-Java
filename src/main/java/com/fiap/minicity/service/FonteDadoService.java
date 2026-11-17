
package com.fiap.minicity.service;

import com.fiap.minicity.dao.FonteDadoDAO;
import com.fiap.minicity.dto.FonteDadoDTO;
import com.fiap.minicity.exception.ConflitoException;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.mapper.FonteDadoMapper;
import com.fiap.minicity.models.FonteDado;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelas regras de negócio das fontes de dados.
 * Realiza validações e utiliza o DAO para acessar os dados.
 */
@Service
public class FonteDadoService {

    /** DAO responsável pelas operações de acesso às fontes de dados. */
    private final FonteDadoDAO dao;

    /**
     * Constrói o serviço recebendo o DAO necessário para suas operações.
     *
     * @param dao objeto responsável pelo acesso aos dados das fontes
     */
    public FonteDadoService(FonteDadoDAO dao) {
        this.dao = dao;
    }

    /**
     * Lista todas as fontes de dados cadastradas.
     * Converte os objetos do modelo para DTO antes de retornar.
     *
     * @return lista de fontes de dados em formato DTO
     */
    public List<FonteDadoDTO> listar() {
        return dao.listar()
                .stream()
                .map(FonteDadoMapper::toDTO)
                .toList();
    }

    /**
     * Busca uma fonte de dados pelo seu ID.
     *
     * @param id identificador da fonte que será buscada
     * @return fonte de dados encontrada em formato DTO
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se a fonte não existir
     */
    public FonteDadoDTO buscarPorId(Long id) {
        validarId(id);

        FonteDado fonte = dao.buscarPorId(id);

        if (fonte == null) {
            throw new RecursoNaoEncontradoException(
                    "Fonte de dado não encontrada."
            );
        }

        return FonteDadoMapper.toDTO(fonte);
    }

    /**
     * Cadastra uma nova fonte de dados após validar os dados informados.
     * Também verifica se já existe outra fonte com o mesmo nome.
     *
     * @param fonteDTO dados da fonte que será cadastrada
     * @return fonte de dados cadastrada em formato DTO
     * @throws RegraNegocioException se os dados informados forem inválidos
     * @throws ConflitoException se já existir uma fonte com o mesmo nome
     */
    public FonteDadoDTO cadastrar(FonteDadoDTO fonteDTO) {
        validarFonte(fonteDTO);

        // Verifica se já existe uma fonte com esse nome
        if (dao.existePorNome(fonteDTO.nomeFonte())) {
            throw new ConflitoException(
                    "Já existe uma fonte de dado cadastrada com esse nome."
            );
        }

        FonteDado fonte = FonteDadoMapper.toModel(fonteDTO);
        fonte.setId(null);

        FonteDado fonteCadastrada = dao.cadastrar(fonte);

        return FonteDadoMapper.toDTO(fonteCadastrada);
    }

    /**
     * Atualiza os dados de uma fonte de dados existente.
     * Verifica se a fonte existe e se outra fonte já utiliza o nome informado.
     *
     * @param id identificador da fonte que será atualizada
     * @param fonteDTO novos dados da fonte
     * @return fonte de dados atualizada em formato DTO
     * @throws RegraNegocioException se o ID ou os dados forem inválidos
     * @throws RecursoNaoEncontradoException se a fonte não existir
     * @throws ConflitoException se outra fonte já utilizar o nome informado
     */
    public FonteDadoDTO atualizar(Long id, FonteDadoDTO fonteDTO) {
        validarId(id);
        validarFonte(fonteDTO);

        // Verifica se a fonte existe
        if (dao.buscarPorId(id) == null) {
            throw new RecursoNaoEncontradoException(
                    "Fonte de dado não encontrada."
            );
        }

        // Verifica se outra fonte já utiliza esse nome
        if (dao.existePorNomeEmOutraFonte(fonteDTO.nomeFonte(), id)) {
            throw new ConflitoException(
                    "Esse nome já está sendo utilizado por outra fonte."
            );
        }

        FonteDado fonte = FonteDadoMapper.toModel(fonteDTO);
        FonteDado fonteAtualizada = dao.atualizar(id, fonte);

        if (fonteAtualizada == null) {
            throw new RecursoNaoEncontradoException(
                    "Fonte de dado não encontrada."
            );
        }

        return FonteDadoMapper.toDTO(fonteAtualizada);
    }

    /**
     * Exclui uma fonte de dados pelo seu ID.
     *
     * @param id identificador da fonte que será excluída
     * @return true se a fonte for excluída com sucesso
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se a fonte não existir
     */
    public boolean deletar(Long id) {
        validarId(id);

        boolean deletado = dao.deletar(id);

        if (!deletado) {
            throw new RecursoNaoEncontradoException(
                    "Fonte de dado não encontrada."
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
            throw new RegraNegocioException(
                    "O ID informado é inválido."
            );
        }
    }

    /**
     * Valida os dados de uma fonte de dados.
     * Verifica se os dados foram informados e se o nome e o tipo não estão vazios.
     *
     * @param fonteDTO dados da fonte que serão validados
     * @throws RegraNegocioException se os dados não forem informados,
     * se o nome estiver vazio ou se o tipo estiver vazio
     */
    private void validarFonte(FonteDadoDTO fonteDTO) {
        if (fonteDTO == null) {
            throw new RegraNegocioException(
                    "Os dados da fonte são obrigatórios."
            );
        }

        if (fonteDTO.nomeFonte() == null || fonteDTO.nomeFonte().isBlank()) {
            throw new RegraNegocioException(
                    "O nome da fonte é obrigatório."
            );
        }

        if (fonteDTO.tipo() == null || fonteDTO.tipo().isBlank()) {
            throw new RegraNegocioException(
                    "O tipo da fonte é obrigatório."
            );
        }
    }
}
