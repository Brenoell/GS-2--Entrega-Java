package com.fiap.minicity.service;

import com.fiap.minicity.dao.CategoriaOcorrenciaDAO;
import com.fiap.minicity.models.CategoriaOcorrencia;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoriaOcorrenciaService {

    private final CategoriaOcorrenciaDAO dao;

    public CategoriaOcorrenciaService(CategoriaOcorrenciaDAO dao) {
        this.dao = dao;
    }

    public List<CategoriaOcorrencia> listar() {
        return dao.listar();
    }

    public CategoriaOcorrencia buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public CategoriaOcorrencia cadastrar(CategoriaOcorrencia objeto) {
        return dao.cadastrar(objeto);
    }

    public CategoriaOcorrencia atualizar(Long id, CategoriaOcorrencia objeto) {
        return dao.atualizar(id, objeto);
    }

    public boolean deletar(Long id) {
        return dao.deletar(id);
    }
}