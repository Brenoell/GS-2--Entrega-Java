package com.fiap.minicity.service;

import com.fiap.minicity.dao.FonteDadoDAO;
import com.fiap.minicity.models.FonteDado;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FonteDadoService {

    private final FonteDadoDAO dao;

    public FonteDadoService(FonteDadoDAO dao) {
        this.dao = dao;
    }

    public List<FonteDado> listar() {
        return dao.listar();
    }

    public FonteDado buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public FonteDado cadastrar(FonteDado objeto) {
        return dao.cadastrar(objeto);
    }

    public FonteDado atualizar(Long id, FonteDado objeto) {
        return dao.atualizar(id, objeto);
    }

    public boolean deletar(Long id) {
        return dao.deletar(id);
    }
}