
package com.fiap.minicity.service;

import com.fiap.minicity.dao.DistritoDAO;
import com.fiap.minicity.dto.DistritoDTO;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.mapper.DistritoMapper;
import com.fiap.minicity.models.Distrito;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DistritoService {

    private final DistritoDAO dao;

    public DistritoService(DistritoDAO dao) {
        this.dao = dao;
    }

    public List<DistritoDTO> listar() {
        return dao.listar()
                .stream()
                .map(DistritoMapper::toDTO)
                .toList();
    }

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

    public DistritoDTO cadastrar(DistritoDTO distritoDTO) {
        validarDistrito(distritoDTO);

        Distrito distrito = DistritoMapper.toModel(distritoDTO);

        // O ID é gerado pelo banco.
        distrito.setId(null);

        Distrito distritoCadastrado = dao.cadastrar(distrito);

        return DistritoMapper.toDTO(distritoCadastrado);
    }

    public DistritoDTO atualizar(Long id, DistritoDTO distritoDTO) {
        validarId(id);
        validarDistrito(distritoDTO);

        Distrito distrito = DistritoMapper.toModel(distritoDTO);

        Distrito distritoAtualizado = dao.atualizar(id, distrito);

        if (distritoAtualizado == null) {
            throw new RecursoNaoEncontradoException(
                    "Distrito não encontrado."
            );
        }

        return DistritoMapper.toDTO(distritoAtualizado);
    }

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

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new RegraNegocioException(
                    "O ID informado é inválido."
            );
        }
    }

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
