
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

@Service
public class CategoriaOcorrenciaService {

    private final CategoriaOcorrenciaDAO dao;

    public CategoriaOcorrenciaService(CategoriaOcorrenciaDAO dao) {
        this.dao = dao;
    }

    public List<CategoriaOcorrenciaDTO> listar() {

        return dao.listar()
                .stream()
                .map(CategoriaOcorrenciaMapper::toDTO)
                .toList();
    }

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

    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new RegraNegocioException("ID inválido.");
        }
    }

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
