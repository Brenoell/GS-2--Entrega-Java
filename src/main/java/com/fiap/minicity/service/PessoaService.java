
package com.fiap.minicity.service;

import com.fiap.minicity.dao.PessoaDAO;
import com.fiap.minicity.dto.LoginDTO;
import com.fiap.minicity.dto.PessoaDTO;
import com.fiap.minicity.exception.ConflitoException;
import com.fiap.minicity.exception.CredenciaisInvalidasException;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.mapper.PessoaMapper;
import com.fiap.minicity.models.Pessoa;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Serviço responsável pelas operações relacionadas às pessoas,
 * incluindo cadastro, consulta, atualização, exclusão e autenticação.
 */
@Service
public class PessoaService {

    /**
     * DAO responsável pelo acesso aos dados das pessoas no banco de dados.
     */
    private final PessoaDAO dao;

    /**
     * Codificador utilizado para criptografar e verificar as senhas.
     */
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    /**
     * Construtor da classe PessoaService.
     *
     * @param dao DAO responsável pelas operações de persistência das pessoas.
     */
    public PessoaService(PessoaDAO dao) {
        this.dao = dao;
    }

    /**
     * Lista todas as pessoas cadastradas e converte os resultados para DTO.
     *
     * @return lista de pessoas representadas por PessoaDTO.
     */
    public List<PessoaDTO> listar() {
        return dao.listar()
                .stream()
                .map(PessoaMapper::toDTO)
                .toList();
    }

    /**
     * Busca uma pessoa pelo seu identificador.
     *
     * @param id identificador da pessoa.
     * @return pessoa encontrada representada por PessoaDTO.
     * @throws RegraNegocioException se o ID for nulo ou menor ou igual a zero.
     * @throws RecursoNaoEncontradoException se a pessoa não for encontrada.
     */
    public PessoaDTO buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new RegraNegocioException("ID inválido.");
        }

        Pessoa pessoa = dao.buscarPorId(id);

        if (pessoa == null) {
            throw new RecursoNaoEncontradoException(
                    "Pessoa não encontrada."
            );
        }

        return PessoaMapper.toDTO(pessoa);
    }

    /**
     * Cadastra uma nova pessoa, validando os dados e criptografando sua senha.
     *
     * @param pessoaDTO dados da pessoa que será cadastrada.
     * @return pessoa cadastrada representada por PessoaDTO.
     * @throws RegraNegocioException se os dados forem nulos ou a senha
     * não atender aos requisitos.
     * @throws ConflitoException se já existir uma pessoa cadastrada
     * com o mesmo e-mail.
     */
    public PessoaDTO cadastrar(PessoaDTO pessoaDTO) {
        if (pessoaDTO == null) {
            throw new RegraNegocioException(
                    "Os dados da pessoa são obrigatórios."
            );
        }

        if (pessoaDTO.senha() == null || pessoaDTO.senha().isBlank()) {
            throw new RegraNegocioException(
                    "A senha é obrigatória."
            );
        }

        if (pessoaDTO.senha().length() < 8) {
            throw new RegraNegocioException(
                    "A senha deve ter pelo menos 8 caracteres."
            );
        }

        if (dao.existePorEmail(pessoaDTO.email())) {
            throw new ConflitoException(
                    "Já existe uma pessoa cadastrada com esse e-mail."
            );
        }

        Pessoa pessoa = PessoaMapper.toModel(pessoaDTO);

        pessoa.setSenha(passwordEncoder.encode(pessoaDTO.senha()));

        Pessoa pessoaCadastrada = dao.cadastrar(pessoa);

        return PessoaMapper.toDTO(pessoaCadastrada);
    }

    /**
     * Atualiza os dados de uma pessoa cadastrada, preservando sua senha atual.
     *
     * @param id identificador da pessoa que será atualizada.
     * @param pessoaDTO novos dados da pessoa.
     * @return pessoa atualizada representada por PessoaDTO.
     * @throws RegraNegocioException se o ID for inválido ou os dados forem nulos.
     * @throws RecursoNaoEncontradoException se a pessoa não for encontrada.
     * @throws ConflitoException se o e-mail já estiver sendo utilizado
     * por outra pessoa.
     */
    public PessoaDTO atualizar(Long id, PessoaDTO pessoaDTO) {
        if (id == null || id <= 0) {
            throw new RegraNegocioException("ID inválido.");
        }

        if (pessoaDTO == null) {
            throw new RegraNegocioException(
                    "Os dados da pessoa são obrigatórios."
            );
        }

        // Busca a pessoa atual para preservar a senha.
        Pessoa pessoaExistente = dao.buscarPorId(id);

        if (pessoaExistente == null) {
            throw new RecursoNaoEncontradoException(
                    "Pessoa não encontrada."
            );
        }

        // Verifica se outra pessoa já utiliza esse e-mail.
        if (dao.existePorEmailEmOutraPessoa(pessoaDTO.email(), id)) {
            throw new ConflitoException(
                    "Esse e-mail já está sendo utilizado por outra pessoa."
            );
        }

        Pessoa pessoa = PessoaMapper.toModel(pessoaDTO);

        // Mantém a senha já cadastrada.
        pessoa.setSenha(pessoaExistente.getSenha());

        Pessoa pessoaAtualizada = dao.atualizar(id, pessoa);

        if (pessoaAtualizada == null) {
            throw new RecursoNaoEncontradoException(
                    "Pessoa não encontrada."
            );
        }

        return PessoaMapper.toDTO(pessoaAtualizada);
    }

    /**
     * Exclui uma pessoa pelo seu identificador.
     *
     * @param id identificador da pessoa que será excluída.
     * @return true se a exclusão for realizada com sucesso.
     * @throws RegraNegocioException se o ID for nulo ou menor ou igual a zero.
     * @throws RecursoNaoEncontradoException se a pessoa não for encontrada.
     */
    public boolean deletar(Long id) {
        if (id == null || id <= 0) {
            throw new RegraNegocioException("ID inválido.");
        }

        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException(
                    "Pessoa não encontrada."
            );
        }

        return true;
    }

    /**
     * Realiza a autenticação de uma pessoa utilizando e-mail e senha.
     *
     * @param loginDTO dados de acesso contendo e-mail e senha.
     * @return pessoa autenticada representada por PessoaDTO.
     * @throws RegraNegocioException se os dados de acesso forem nulos
     * ou obrigatórios não forem informados.
     * @throws CredenciaisInvalidasException se o e-mail ou a senha
     * estiverem incorretos.
     */
    public PessoaDTO login(LoginDTO loginDTO) {

        if (loginDTO == null ||
                loginDTO.email() == null ||
                loginDTO.senha() == null) {

            throw new RegraNegocioException(
                    "E-mail e senha são obrigatórios."
            );
        }

        Pessoa pessoa = dao.buscarPorEmail(loginDTO.email());

        if (pessoa == null ||
                pessoa.getSenha() == null ||
                !passwordEncoder.matches(
                        loginDTO.senha(),
                        pessoa.getSenha()
                )) {

            throw new CredenciaisInvalidasException(
                    "E-mail ou senha incorretos."
            );
        }

        return PessoaMapper.toDTO(pessoa);
    }
}
