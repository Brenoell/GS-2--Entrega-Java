package com.fiap.minicity.service;

import com.fiap.minicity.dao.PessoaDAO;
import com.fiap.minicity.models.Pessoa;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    private final PessoaDAO dao;

    public PessoaService(PessoaDAO dao) {
        this.dao = dao;
    }

    public List<Pessoa> listar() {
        return dao.listar();
    }

    public Pessoa buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public Pessoa cadastrar(Pessoa objeto) {
        return dao.cadastrar(objeto);
    }

    public Pessoa atualizar(Long id, Pessoa objeto) {
        return dao.atualizar(id, objeto);
    }

    public boolean deletar(Long id) {
        return dao.deletar(id);
    }
}