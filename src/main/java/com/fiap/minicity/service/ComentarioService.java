package com.fiap.minicity.service;

import com.fiap.minicity.dao.ComentarioDAO;
import com.fiap.minicity.models.Comentario;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {

    private final ComentarioDAO dao;

    public ComentarioService(ComentarioDAO dao) {
        this.dao = dao;
    }

    public List<Comentario> listar() {
        return dao.listar();
    }

    public Comentario buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public Comentario cadastrar(Comentario objeto) {
        return dao.cadastrar(objeto);
    }

    public Comentario atualizar(Long id, Comentario objeto) {
        return dao.atualizar(id, objeto);
    }

    public boolean deletar(Long id) {
        return dao.deletar(id);
    }
}