package com.fiap.minicity.service;

import com.fiap.minicity.dao.OcorrenciaDAO;
import com.fiap.minicity.models.Ocorrencia;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OcorrenciaService {

    private final OcorrenciaDAO dao;

    public OcorrenciaService(OcorrenciaDAO dao) {
        this.dao = dao;
    }

    public List<Ocorrencia> listar() {
        return dao.listar();
    }

    public Ocorrencia buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public Ocorrencia cadastrar(Ocorrencia objeto) {
        return dao.cadastrar(objeto);
    }

    public Ocorrencia atualizar(Long id, Ocorrencia objeto) {
        return dao.atualizar(id, objeto);
    }

    public boolean deletar(Long id) {
        return dao.deletar(id);
    }
}