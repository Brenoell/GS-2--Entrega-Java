
package com.fiap.minicity.dao;

import com.fiap.minicity.models.Distrito;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar as operações de acesso ao banco de dados
 * relacionadas aos distritos.
 */
@Repository
public class DistritoDAO {

    /**
     * Lista todos os distritos cadastrados no banco de dados.
     *
     * @return lista contendo todos os distritos
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
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

    /**
     * Busca um distrito pelo seu identificador.
     *
     * @param id identificador do distrito
     * @return distrito encontrado ou null caso não exista
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
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

    /**
     * Verifica se já existe um distrito com o nome informado.
     * A comparação desconsidera diferenças entre letras maiúsculas,
     * minúsculas e espaços nas extremidades.
     *
     * @param nome nome do distrito que será verificado
     * @return true se já existir um distrito com esse nome, false caso contrário
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
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

    /**
     * Verifica se outro distrito, diferente do informado, já utiliza
     * o nome recebido.
     *
     * @param nome nome do distrito que será verificado
     * @param id identificador do distrito que deve ser ignorado na comparação
     * @return true se outro distrito já utilizar esse nome, false caso contrário
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
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

    /**
     * Cadastra um novo distrito no banco de dados.
     * Após a inserção, recupera o identificador gerado pelo banco.
     *
     * @param d objeto contendo os dados do distrito que será cadastrado
     * @return objeto do distrito cadastrado com seu identificador preenchido
     * @throws RuntimeException caso ocorra um erro ao inserir os dados
     */
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

    /**
     * Atualiza os dados de um distrito existente.
     *
     * @param id identificador do distrito que será atualizado
     * @param d objeto contendo os novos dados do distrito
     * @return objeto atualizado ou null caso o distrito não seja encontrado
     * @throws RuntimeException caso ocorra um erro ao atualizar os dados
     */
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

    /**
     * Remove um distrito do banco de dados pelo seu identificador.
     *
     * @param id identificador do distrito que será removido
     * @return true se o distrito foi removido, false caso não tenha sido encontrado
     * @throws RuntimeException caso ocorra um erro ao excluir os dados
     */
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

    /**
     * Preenche os parâmetros do PreparedStatement com os dados do distrito.
     *
     * @param ps comando SQL que receberá os valores
     * @param d objeto contendo os dados do distrito
     * @throws SQLException caso ocorra um erro ao preencher os parâmetros
     */
    private void preencher(PreparedStatement ps, Distrito d)
            throws SQLException {

        ps.setString(1, d.getNomeDistrito());
        ps.setString(2, d.getZona());
        ps.setDouble(3, d.getLatitude());
        ps.setDouble(4, d.getLongitude());
    }

    /**
     * Converte os dados de uma linha do ResultSet em um objeto Distrito.
     *
     * @param rs resultado da consulta SQL
     * @return objeto Distrito preenchido com os dados da linha consultada
     * @throws SQLException caso ocorra um erro ao acessar os dados do resultado
     */
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
