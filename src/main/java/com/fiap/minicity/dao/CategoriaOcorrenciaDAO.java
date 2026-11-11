package com.fiap.minicity.dao;

import com.fiap.minicity.models.CategoriaOcorrencia;
import java.sql.*;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaOcorrenciaDAO {

    public List<CategoriaOcorrencia> listar() {
        List<CategoriaOcorrencia> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nm_ocorrencia, ds_texto FROM CATEGORIA_OCORRENCIA ORDER BY id_categoria";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));
            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias.", e);
        }
    }

    public CategoriaOcorrencia buscarPorId(Long id) {
        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id_categoria, nm_ocorrencia, ds_texto FROM CATEGORIA_OCORRENCIA WHERE id_categoria=?")) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar categoria.", e);
        }
    }

    public CategoriaOcorrencia cadastrar(CategoriaOcorrencia x) {
        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO CATEGORIA_OCORRENCIA (nm_ocorrencia, ds_texto) VALUES (?,?)",
                     new String[]{"ID_CATEGORIA"})) {

            preencher(ps, x);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) x.setId(rs.getLong(1));
            }

            return x;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar categoria.", e);
        }
    }

    public CategoriaOcorrencia atualizar(Long id, CategoriaOcorrencia x) {
        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE CATEGORIA_OCORRENCIA SET nm_ocorrencia=?, ds_texto=? WHERE id_categoria=?")) {

            preencher(ps, x);
            ps.setLong(3, id);

            if (ps.executeUpdate() == 0) return null;

            x.setId(id);
            return x;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria.", e);
        }
    }

    public boolean deletar(Long id) {
        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM CATEGORIA_OCORRENCIA WHERE id_categoria=?")) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar categoria.", e);
        }
    }

    private void preencher(PreparedStatement ps, CategoriaOcorrencia x) throws SQLException {
        ps.setString(1, x.getNomeOcorrencia());
        ps.setString(2, x.getDescTexto());
    }

    private CategoriaOcorrencia mapear(ResultSet rs) throws SQLException {
        return new CategoriaOcorrencia(
                rs.getLong("id_categoria"),
                rs.getString("nm_ocorrencia"),
                rs.getString("ds_texto")
        );
    }
}