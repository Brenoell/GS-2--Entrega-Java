
package com.fiap.minicity.dao;

import com.fiap.minicity.models.FonteDado;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar as operações de acesso ao banco de dados
 * relacionadas às fontes de dados.
 */
@Repository
public class FonteDadoDAO {

    /**
     * Lista todas as fontes de dados cadastradas no banco.
     *
     * @return lista contendo todas as fontes de dados
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
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

    /**
     * Busca uma fonte de dados pelo seu identificador.
     *
     * @param id identificador da fonte de dados
     * @return fonte encontrada ou null caso não exista
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
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

    /**
     * Verifica se já existe uma fonte de dados com o nome informado.
     * A comparação desconsidera diferenças entre letras maiúsculas,
     * minúsculas e espaços nas extremidades.
     *
     * @param nome nome da fonte que será verificado
     * @return true se já existir uma fonte com esse nome, false caso contrário
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
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

    /**
     * Verifica se outra fonte de dados, diferente da informada,
     * já utiliza o nome recebido.
     *
     * @param nome nome da fonte que será verificado
     * @param id identificador da fonte que deve ser ignorada na comparação
     * @return true se outra fonte já utilizar esse nome, false caso contrário
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
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

    /**
     * Cadastra uma nova fonte de dados no banco.
     * Após a inserção, recupera o identificador gerado pelo banco.
     *
     * @param x objeto contendo os dados da fonte que será cadastrada
     * @return objeto da fonte cadastrada com seu identificador preenchido
     * @throws RuntimeException caso ocorra um erro ao inserir os dados
     */
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

    /**
     * Atualiza os dados de uma fonte de dados existente.
     *
     * @param id identificador da fonte que será atualizada
     * @param x objeto contendo os novos dados da fonte
     * @return objeto atualizado ou null caso a fonte não seja encontrada
     * @throws RuntimeException caso ocorra um erro ao atualizar os dados
     */
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

    /**
     * Remove uma fonte de dados do banco pelo seu identificador.
     *
     * @param id identificador da fonte que será removida
     * @return true se a fonte foi removida, false caso não tenha sido encontrada
     * @throws RuntimeException caso ocorra um erro ao excluir os dados
     */
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

    /**
     * Preenche os parâmetros do PreparedStatement com os dados da fonte.
     *
     * @param ps comando SQL que receberá os valores
     * @param x objeto contendo os dados da fonte
     * @throws SQLException caso ocorra um erro ao preencher os parâmetros
     */
    private void preencher(PreparedStatement ps, FonteDado x)
            throws SQLException {

        ps.setString(1, x.getNomeFonte());
        ps.setString(2, x.getTipo());
        ps.setString(3, x.getUrl());
    }

    /**
     * Converte os dados de uma linha do ResultSet em um objeto FonteDado.
     *
     * @param rs resultado da consulta SQL
     * @return objeto FonteDado preenchido com os dados da linha consultada
     * @throws SQLException caso ocorra um erro ao acessar os dados do resultado
     */
    private FonteDado mapear(ResultSet rs) throws SQLException {
        return new FonteDado(
                rs.getLong("id_fonte"),
                rs.getString("nm_fonte"),
                rs.getString("ds_tipo"),
                rs.getString("ds_url")
        );
    }
}
