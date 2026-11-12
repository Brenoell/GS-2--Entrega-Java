
package com.fiap.minicity.service;

import com.fiap.minicity.dao.FonteDadoDAO;
import com.fiap.minicity.dto.FonteDadoDTO;
import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;
import com.fiap.minicity.mapper.FonteDadoMapper;
import com.fiap.minicity.models.FonteDado;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FonteDadoService {

    private final FonteDadoDAO dao;

    public FonteDadoService(FonteDadoDAO dao) {
        this.dao = dao;
    }

    public List<FonteDadoDTO> listar() {
        return dao.listar()
                .stream()
                .map(FonteDadoMapper::toDTO)
                .toList();
    }

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

    public FonteDadoDTO cadastrar(FonteDadoDTO fonteDTO) {
        validarFonte(fonteDTO);

        FonteDado fonte = FonteDadoMapper.toModel(fonteDTO);
        fonte.setId(null);

        FonteDado fonteCadastrada = dao.cadastrar(fonte);

        return FonteDadoMapper.toDTO(fonteCadastrada);
    }

    public FonteDadoDTO atualizar(Long id, FonteDadoDTO fonteDTO) {
        validarId(id);
        validarFonte(fonteDTO);

        FonteDado fonte = FonteDadoMapper.toModel(fonteDTO);
        FonteDado fonteAtualizada = dao.atualizar(id, fonte);

        if (fonteAtualizada == null) {
            throw new RecursoNaoEncontradoException(
                    "Fonte de dado não encontrada."
            );
        }

        return FonteDadoMapper.toDTO(fonteAtualizada);
    }

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

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new RegraNegocioException(
                    "O ID informado é inválido."
            );
        }
    }

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
