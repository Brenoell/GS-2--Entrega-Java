package com.fiap.minicity.service;

import com.fiap.minicity.dao.DistritoDAO;
import com.fiap.minicity.models.Distrito;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DistritoService {

    private final DistritoDAO dao;

    public DistritoService(DistritoDAO dao) {
        this.dao = dao;
    }

    public List<Distrito> listar() {
        return dao.listar();
    }

    public Distrito buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public Distrito cadastrar(Distrito distrito) {
        return dao.cadastrar(distrito);
    }

    public Distrito atualizar(Long id, Distrito distrito) {
        return dao.atualizar(id, distrito);
    }

    public boolean deletar(Long id) {
        return dao.deletar(id);
    }
}