
package com.fiap.minicity.service;

import com.fiap.minicity.dao.OcorrenciaDAO;
import com.fiap.minicity.dto.OcorrenciaDTO;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.mapper.OcorrenciaMapper;
import com.fiap.minicity.models.Ocorrencia;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OcorrenciaService {

    private final OcorrenciaDAO dao;

    public OcorrenciaService(OcorrenciaDAO dao) {
        this.dao = dao;
    }

    public List<OcorrenciaDTO> listar() {
        return dao.listar()
                .stream()
                .map(OcorrenciaMapper::toDTO)
                .toList();
    }

    public OcorrenciaDTO buscarPorId(Long id) {

        validarId(id);

        Ocorrencia ocorrencia = dao.buscarPorId(id);

        if (ocorrencia == null) {
            throw new RecursoNaoEncontradoException(
                    "Ocorrência não encontrada."
            );
        }

        return OcorrenciaMapper.toDTO(ocorrencia);
    }

    public OcorrenciaDTO cadastrar(OcorrenciaDTO objeto) {

        validarOcorrencia(objeto);

        Ocorrencia ocorrencia = OcorrenciaMapper.toModel(objeto);

        Ocorrencia cadastrada = dao.cadastrar(ocorrencia);

        return OcorrenciaMapper.toDTO(cadastrada);
    }

    public OcorrenciaDTO atualizar(Long id, OcorrenciaDTO objeto) {

        validarId(id);
        validarOcorrencia(objeto);

        Ocorrencia ocorrencia = OcorrenciaMapper.toModel(objeto);

        Ocorrencia atualizada = dao.atualizar(id, ocorrencia);

        if (atualizada == null) {
            throw new RecursoNaoEncontradoException(
                    "Ocorrência não encontrada."
            );
        }

        return OcorrenciaMapper.toDTO(atualizada);
    }

    public boolean deletar(Long id) {

        validarId(id);

        boolean deletada = dao.deletar(id);

        if (!deletada) {
            throw new RecursoNaoEncontradoException(
                    "Ocorrência não encontrada."
            );
        }

        return true;
    }

    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new RegraNegocioException("ID inválido.");
        }
    }

    private void validarOcorrencia(OcorrenciaDTO objeto) {

        if (objeto == null) {
            throw new RegraNegocioException(
                    "Ocorrência não informada."
            );
        }

        if (objeto.nomeTitulo() == null || objeto.nomeTitulo().isBlank()) {
            throw new RegraNegocioException(
                    "O título da ocorrência é obrigatório."
            );
        }

        if (objeto.nomeTitulo().length() > 120) {
            throw new RegraNegocioException(
                    "O título deve ter no máximo 120 caracteres."
            );
        }

        if (objeto.gravidade() == null ||
                objeto.gravidade() < 1 || objeto.gravidade() > 5) {
            throw new RegraNegocioException(
                    "A gravidade deve estar entre 1 e 5."
            );
        }

        if (objeto.origem() == null ||
                (!objeto.origem().equals("API") &&
                        !objeto.origem().equals("CIDADAO"))) {
            throw new RegraNegocioException(
                    "A origem deve ser API ou CIDADAO."
            );
        }

        if (objeto.status() != null &&
                !List.of("ATIVA", "EM_ANALISE", "RESOLVIDA", "EXPIRADA")
                        .contains(objeto.status())) {
            throw new RegraNegocioException("Status inválido.");
        }

        if (objeto.latitude() == null ||
                !Double.isFinite(objeto.latitude()) ||
                objeto.latitude() < -90 || objeto.latitude() > 90) {
            throw new RegraNegocioException("Latitude inválida.");
        }

        if (objeto.longitude() == null ||
                !Double.isFinite(objeto.longitude()) ||
                objeto.longitude() < -180 || objeto.longitude() > 180) {
            throw new RegraNegocioException("Longitude inválida.");
        }

        if (objeto.dataOcorrencia() == null) {
            throw new RegraNegocioException(
                    "A data da ocorrência é obrigatória."
            );
        }

        if (objeto.distrito() == null ||
                objeto.distrito().id() == null ||
                objeto.distrito().id() <= 0) {
            throw new RegraNegocioException(
                    "É necessário informar um distrito válido."
            );
        }

        if (objeto.categoria() == null ||
                objeto.categoria().id() == null ||
                objeto.categoria().id() <= 0) {
            throw new RegraNegocioException(
                    "É necessário informar uma categoria válida."
            );
        }

        if ("API".equals(objeto.origem())) {

            if (objeto.fonte() == null ||
                    objeto.fonte().id() == null ||
                    objeto.fonte().id() <= 0) {
                throw new RegraNegocioException(
                        "Ocorrências de API precisam de uma fonte válida."
                );
            }

            if (objeto.pessoa() != null) {
                throw new RegraNegocioException(
                        "Ocorrências de API não podem possuir uma pessoa."
                );
            }

        } else {

            if (objeto.pessoa() == null ||
                    objeto.pessoa().id() == null ||
                    objeto.pessoa().id() <= 0) {
                throw new RegraNegocioException(
                        "Ocorrências de cidadãos precisam de uma pessoa válida."
                );
            }

            if (objeto.fonte() != null) {
                throw new RegraNegocioException(
                        "Ocorrências de cidadãos não podem possuir uma fonte."
                );
            }
        }
    }
}
