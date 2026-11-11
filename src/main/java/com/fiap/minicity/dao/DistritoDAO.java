package com.fiap.minicity.dao;

import com.fiap.minicity.models.Distrito;
import java.sql.*;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class DistritoDAO {

    public List<Distrito> listar() {
        List<Distrito> lista = new ArrayList<>();

        String sql = "SELECT * FROM DISTRITO";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Distrito d = new Distrito();

                d.setId(rs.getLong("id_distrito"));
                d.setNomeDistrito(rs.getString("nm_distrito"));
                d.setZona(rs.getString("ds_zona"));
                d.setLatitude(rs.getDouble("ds_latitude"));
                d.setLongitude(rs.getDouble("ds_longitude"));

                lista.add(d);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public Distrito buscarPorId(Long id) {
        String sql = "SELECT * FROM DISTRITO WHERE id_distrito = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Distrito d = new Distrito();

                d.setId(rs.getLong("id_distrito"));
                d.setNomeDistrito(rs.getString("nm_distrito"));
                d.setZona(rs.getString("ds_zona"));
                d.setLatitude(rs.getDouble("ds_latitude"));
                d.setLongitude(rs.getDouble("ds_longitude"));

                return d;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Distrito cadastrar(Distrito d) {
        String sql = "INSERT INTO DISTRITO (nm_distrito, ds_zona, ds_latitude, ds_longitude) VALUES (?, ?, ?, ?)";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql, new String[]{"ID_DISTRITO"})) {

            ps.setString(1, d.getNomeDistrito());
            ps.setString(2, d.getZona());
            ps.setDouble(3, d.getLatitude());
            ps.setDouble(4, d.getLongitude());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                d.setId(rs.getLong(1));
            }

            return d;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Distrito atualizar(Long id, Distrito d) {
        String sql = "UPDATE DISTRITO SET nm_distrito = ?, ds_zona = ?, ds_latitude = ?, ds_longitude = ? WHERE id_distrito = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, d.getNomeDistrito());
            ps.setString(2, d.getZona());
            ps.setDouble(3, d.getLatitude());
            ps.setDouble(4, d.getLongitude());
            ps.setLong(5, id);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                d.setId(id);
                return d;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM DISTRITO WHERE id_distrito = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}