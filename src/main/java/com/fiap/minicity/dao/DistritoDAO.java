
package com.fiap.minicity.dao;

import com.fiap.minicity.models.Distrito;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DistritoDAO {

    public List<Distrito> listar() {
        List<Distrito> lista = new ArrayList<>();

        String sql = "SELECT id_distrito, nm_distrito, ds_zona, " +
                "ds_latitude, ds_longitude " +
                "FROM DISTRITO ORDER BY id_distrito";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar distritos.", e);
        }
    }

    public Distrito buscarPorId(Long id) {
        String sql = "SELECT id_distrito, nm_distrito, ds_zona, " +
                "ds_latitude, ds_longitude " +
                "FROM DISTRITO WHERE id_distrito = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar distrito.", e);
        }
    }

    // Verifica se já existe um distrito com esse nome
    public boolean existePorNome(String nome) {
        String sql = "SELECT COUNT(*) FROM DISTRITO " +
                "WHERE UPPER(TRIM(nm_distrito)) = UPPER(TRIM(?))";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao verificar nome do distrito.", e
            );
        }
    }

    // Verifica se outro distrito já utiliza esse nome
    public boolean existePorNomeEmOutroDistrito(String nome, Long id) {
        String sql = "SELECT COUNT(*) FROM DISTRITO " +
                "WHERE UPPER(TRIM(nm_distrito)) = UPPER(TRIM(?)) " +
                "AND id_distrito <> ?";

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
                    "Erro ao verificar nome do distrito.", e
            );
        }
    }

    public Distrito cadastrar(Distrito d) {
        String sql = "INSERT INTO DISTRITO " +
                "(nm_distrito, ds_zona, ds_latitude, ds_longitude) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     sql, new String[]{"ID_DISTRITO"})) {

            preencher(ps, d);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    d.setId(rs.getLong(1));
                }
            }

            return d;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar distrito.", e);
        }
    }

    public Distrito atualizar(Long id, Distrito d) {
        String sql = "UPDATE DISTRITO SET nm_distrito = ?, " +
                "ds_zona = ?, ds_latitude = ?, ds_longitude = ? " +
                "WHERE id_distrito = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            preencher(ps, d);
            ps.setLong(5, id);

            if (ps.executeUpdate() == 0) {
                return null;
            }

            d.setId(id);

            return d;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar distrito.", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM DISTRITO WHERE id_distrito = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar distrito.", e);
        }
    }

    private void preencher(PreparedStatement ps, Distrito d)
            throws SQLException {

        ps.setString(1, d.getNomeDistrito());
        ps.setString(2, d.getZona());
        ps.setDouble(3, d.getLatitude());
        ps.setDouble(4, d.getLongitude());
    }

    private Distrito mapear(ResultSet rs) throws SQLException {
        return new Distrito(
                rs.getLong("id_distrito"),
                rs.getString("nm_distrito"),
                rs.getString("ds_zona"),
                rs.getDouble("ds_latitude"),
                rs.getDouble("ds_longitude")
        );
    }
}
