
package com.fiap.minicity.dao;

import com.fiap.minicity.models.FonteDado;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FonteDadoDAO {

    public List<FonteDado> listar() {
        List<FonteDado> lista = new ArrayList<>();

        String sql = "SELECT id_fonte, nm_fonte, ds_tipo, ds_url " +
                "FROM FONTE_DADO ORDER BY id_fonte";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar fontes.", e);
        }
    }

    public FonteDado buscarPorId(Long id) {
        String sql = "SELECT id_fonte, nm_fonte, ds_tipo, ds_url " +
                "FROM FONTE_DADO WHERE id_fonte = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar fonte.", e);
        }
    }

    // Verifica se já existe uma fonte com esse nome
    public boolean existePorNome(String nome) {
        String sql = "SELECT COUNT(*) FROM FONTE_DADO " +
                "WHERE UPPER(TRIM(nm_fonte)) = UPPER(TRIM(?))";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao verificar nome da fonte.", e
            );
        }
    }

    // Verifica se outra fonte já utiliza esse nome
    public boolean existePorNomeEmOutraFonte(String nome, Long id) {
        String sql = "SELECT COUNT(*) FROM FONTE_DADO " +
                "WHERE UPPER(TRIM(nm_fonte)) = UPPER(TRIM(?)) " +
                "AND id_fonte <> ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nome);
            ps.setLong(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao verificar nome da fonte.", e
            );
        }
    }

    public FonteDado cadastrar(FonteDado x) {
        String sql = "INSERT INTO FONTE_DADO(nm_fonte, ds_tipo, ds_url) " +
                "VALUES (?, ?, ?)";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     sql, new String[]{"ID_FONTE"})) {

            preencher(ps, x);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    x.setId(rs.getLong(1));
                }
            }

            return x;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar fonte.", e);
        }
    }

    public FonteDado atualizar(Long id, FonteDado x) {
        String sql = "UPDATE FONTE_DADO SET nm_fonte = ?, ds_tipo = ?, " +
                "ds_url = ? WHERE id_fonte = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            preencher(ps, x);
            ps.setLong(4, id);

            if (ps.executeUpdate() == 0) {
                return null;
            }

            x.setId(id);

            return x;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar fonte.", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM FONTE_DADO WHERE id_fonte = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar fonte.", e);
        }
    }

    private void preencher(PreparedStatement ps, FonteDado x)
            throws SQLException {

        ps.setString(1, x.getNomeFonte());
        ps.setString(2, x.getTipo());
        ps.setString(3, x.getUrl());
    }

    private FonteDado mapear(ResultSet rs) throws SQLException {
        return new FonteDado(
                rs.getLong("id_fonte"),
                rs.getString("nm_fonte"),
                rs.getString("ds_tipo"),
                rs.getString("ds_url")
        );
    }
}
