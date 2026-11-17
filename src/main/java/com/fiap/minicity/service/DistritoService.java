
package com.fiap.minicity.service;

import com.fiap.minicity.dao.DistritoDAO;
import com.fiap.minicity.dto.DistritoDTO;
import com.fiap.minicity.exception.ConflitoException;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.mapper.DistritoMapper;
import com.fiap.minicity.models.Distrito;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelas regras de negócio dos distritos.
 * Realiza validações e utiliza o DAO para acessar os dados.
 */
@Service
public class DistritoService {

    /** DAO responsável pelas operações de acesso aos distritos. */
    private final DistritoDAO dao;

    /**
     * Constrói o serviço recebendo o DAO necessário para suas operações.
     *
     * @param dao objeto responsável pelo acesso aos dados dos distritos
     */
    public DistritoService(DistritoDAO dao) {
        this.dao = dao;
    }

    /**
     * Lista todos os distritos cadastrados.
     * Converte os objetos do modelo para DTO antes de retornar.
     *
     * @return lista de distritos em formato DTO
     */
    public List<DistritoDTO> listar() {
        return dao.listar()
                .stream()
                .map(DistritoMapper::toDTO)
                .toList();
    }

    /**
     * Busca um distrito pelo seu ID.
     *
     * @param id identificador do distrito que será buscado
     * @return distrito encontrado em formato DTO
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se o distrito não existir
     */
    public DistritoDTO buscarPorId(Long id) {
        validarId(id);

        Distrito distrito = dao.buscarPorId(id);

        if (distrito == null) {
            throw new RecursoNaoEncontradoException(
                    "Distrito não encontrado."
            );
        }

        return DistritoMapper.toDTO(distrito);
    }

    /**
     * Cadastra um novo distrito após validar os dados informados.
     * Também verifica se já existe outro distrito com o mesmo nome.
     *
     * @param distritoDTO dados do distrito que será cadastrado
     * @return distrito cadastrado em formato DTO
     * @throws RegraNegocioException se os dados informados forem inválidos
     * @throws ConflitoException se já existir um distrito com o mesmo nome
     */
    public DistritoDTO cadastrar(DistritoDTO distritoDTO) {
        validarDistrito(distritoDTO);

        // Verifica se já existe um distrito com esse nome
        if (dao.existePorNome(distritoDTO.nomeDistrito())) {
            throw new ConflitoException(
                    "Já existe um distrito cadastrado com esse nome."
            );
        }

        Distrito distrito = DistritoMapper.toModel(distritoDTO);

        // O ID é gerado pelo banco
        distrito.setId(null);

        Distrito distritoCadastrado = dao.cadastrar(distrito);

        return DistritoMapper.toDTO(distritoCadastrado);
    }

    /**
     * Atualiza os dados de um distrito existente.
     * Verifica se o distrito existe e se outro distrito já utiliza o nome informado.
     *
     * @param id identificador do distrito que será atualizado
     * @param distritoDTO novos dados do distrito
     * @return distrito atualizado em formato DTO
     * @throws RegraNegocioException se o ID ou os dados forem inválidos
     * @throws RecursoNaoEncontradoException se o distrito não existir
     * @throws ConflitoException se outro distrito já utilizar o nome informado
     */
    public DistritoDTO atualizar(Long id, DistritoDTO distritoDTO) {
        validarId(id);
        validarDistrito(distritoDTO);

        // Verifica se o distrito existe
        if (dao.buscarPorId(id) == null) {
            throw new RecursoNaoEncontradoException(
                    "Distrito não encontrado."
            );
        }

        // Verifica se outro distrito já utiliza esse nome
        if (dao.existePorNomeEmOutroDistrito(
                distritoDTO.nomeDistrito(), id)) {
            throw new ConflitoException(
                    "Esse nome já está sendo utilizado por outro distrito."
            );
        }

        Distrito distrito = DistritoMapper.toModel(distritoDTO);

        Distrito distritoAtualizado = dao.atualizar(id, distrito);

        if (distritoAtualizado == null) {
            throw new RecursoNaoEncontradoException(
                    "Distrito não encontrado."
            );
        }

        return DistritoMapper.toDTO(distritoAtualizado);
    }

    /**
     * Exclui um distrito pelo seu ID.
     *
     * @param id identificador do distrito que será excluído
     * @return true se o distrito for excluído com sucesso
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se o distrito não existir
     */
    public boolean deletar(Long id) {
        validarId(id);

        boolean deletado = dao.deletar(id);

        if (!deletado) {
            throw new RecursoNaoEncontradoException(
                    "Distrito não encontrado."
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
     * Valida os dados de um distrito.
     * Verifica o nome, a zona e os valores das coordenadas geográficas.
     *
     * @param distritoDTO dados do distrito que serão validados
     * @throws RegraNegocioException se os dados não forem informados,
     * se o nome ou a zona estiverem vazios ou se as coordenadas forem inválidas
     */
    private void validarDistrito(DistritoDTO distritoDTO) {
        if (distritoDTO == null) {
            throw new RegraNegocioException(
                    "Os dados do distrito são obrigatórios."
            );
        }

        if (distritoDTO.nomeDistrito() == null
                || distritoDTO.nomeDistrito().isBlank()) {
            throw new RegraNegocioException(
                    "O nome do distrito é obrigatório."
            );
        }

        if (distritoDTO.zona() == null || distritoDTO.zona().isBlank()) {
            throw new RegraNegocioException(
                    "A zona do distrito é obrigatória."
            );
        }

        if (!coordenadaValida(distritoDTO.latitude(), true)) {
            throw new RegraNegocioException(
                    "A latitude do distrito é inválida."
            );
        }

        if (!coordenadaValida(distritoDTO.longitude(), false)) {
            throw new RegraNegocioException(
                    "A longitude do distrito é inválida."
            );
        }
    }

    /**
     * Verifica se uma coordenada geográfica está dentro dos limites permitidos.
     *
     * @param valor valor numérico da coordenada
     * @param latitude indica se a coordenada é latitude; caso contrário, é longitude
     * @return true se a coordenada for válida; caso contrário, false
     */
    private boolean coordenadaValida(double valor, boolean latitude) {
        if (!Double.isFinite(valor)) {
            return false;
        }

        if (latitude) {
            return valor >= -90 && valor <= 90;
        }

        return valor >= -180 && valor <= 180;
    }
}
