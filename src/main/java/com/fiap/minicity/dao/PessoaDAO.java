package com.fiap.minicity.dao;

import com.fiap.minicity.models.Pessoa;
import java.sql.*;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class PessoaDAO {

    public List<Pessoa> listar() {
        List<Pessoa> lista = new ArrayList<>();

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id_pessoa,nm_nome,ds_email,ds_perfil,dt_cadastro FROM PESSOA ORDER BY id_pessoa");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapear(rs));
            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pessoas.", e);
        }
    }

    public Pessoa buscarPorId(Long id) {
        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id_pessoa,nm_nome,ds_email,ds_perfil,dt_cadastro FROM PESSOA WHERE id_pessoa=?")) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoa.", e);
        }
    }

    public Pessoa cadastrar(Pessoa x) {
        String sql = "INSERT INTO PESSOA(nm_nome,ds_email,ds_perfil) VALUES(?,?,?)";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql, new String[]{"ID_PESSOA"})) {

            ps.setString(1, x.getNome());
            ps.setString(2, x.getEmail());
            ps.setString(3, x.getPerfil() == null ? "MORADOR" : x.getPerfil());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) x.setId(rs.getLong(1));
            }

            return x;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar pessoa.", e);
        }
    }

    public Pessoa atualizar(Long id, Pessoa x) {
        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE PESSOA SET nm_nome=?,ds_email=?,ds_perfil=? WHERE id_pessoa=?")) {

            ps.setString(1, x.getNome());
            ps.setString(2, x.getEmail());
            ps.setString(3, x.getPerfil());
            ps.setLong(4, id);

            if (ps.executeUpdate() == 0) return null;

            x.setId(id);
            return x;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pessoa.", e);
        }
    }

    public boolean deletar(Long id) {
        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM PESSOA WHERE id_pessoa=?")) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pessoa.", e);
        }
    }

    private Pessoa mapear(ResultSet rs) throws SQLException {
        return new Pessoa(
                rs.getLong("id_pessoa"),
                rs.getString("nm_nome"),
                rs.getString("ds_email"),
                rs.getString("ds_perfil"),
                DAOUtil.data(rs, "dt_cadastro")
        );
    }
}