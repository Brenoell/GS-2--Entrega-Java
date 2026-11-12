
package com.fiap.minicity.service;

import com.fiap.minicity.dao.PessoaDAO;
import com.fiap.minicity.dto.LoginDTO;
import com.fiap.minicity.dto.PessoaDTO;
import com.fiap.minicity.exception.CredenciaisInvalidasException;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.mapper.PessoaMapper;
import com.fiap.minicity.models.Pessoa;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class PessoaService {

    private final PessoaDAO dao;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public PessoaService(PessoaDAO dao) {
        this.dao = dao;
    }

    public List<PessoaDTO> listar() {
        return dao.listar()
                .stream()
                .map(PessoaMapper::toDTO)
                .toList();
    }

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

        Pessoa pessoa = PessoaMapper.toModel(pessoaDTO);

        // Criptografa a senha antes de salvar no banco.
        pessoa.setSenha(passwordEncoder.encode(pessoaDTO.senha()));

        Pessoa pessoaCadastrada = dao.cadastrar(pessoa);

        return PessoaMapper.toDTO(pessoaCadastrada);
    }

    public PessoaDTO atualizar(Long id, PessoaDTO pessoaDTO) {
        if (id == null || id <= 0) {
            throw new RegraNegocioException("ID inválido.");
        }

        if (pessoaDTO == null) {
            throw new RegraNegocioException(
                    "Os dados da pessoa são obrigatórios."
            );
        }

        Pessoa pessoa = PessoaMapper.toModel(pessoaDTO);

        Pessoa pessoaAtualizada = dao.atualizar(id, pessoa);

        if (pessoaAtualizada == null) {
            throw new RecursoNaoEncontradoException(
                    "Pessoa não encontrada."
            );
        }

        return PessoaMapper.toDTO(pessoaAtualizada);
    }

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
