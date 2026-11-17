
package com.fiap.minicity.dao;

import com.fiap.minicity.models.CategoriaOcorrencia;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar as operações de acesso ao banco de dados
 * relacionadas às categorias de ocorrência.
 */
@Repository
public class CategoriaOcorrenciaDAO {

    /**
     * Lista todas as categorias de ocorrência cadastradas no banco de dados.
     *
     * @return lista contendo todas as categorias de ocorrência
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
    public List<CategoriaOcorrencia> listar() {

        List<CategoriaOcorrencia> lista = new ArrayList<>();

        String sql = """
                SELECT id_categoria, nm_ocorrencia, ds_texto
                FROM CATEGORIA_OCORRENCIA
                ORDER BY id_categoria
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias.", e);
        }
    }

    /**
     * Busca uma categoria de ocorrência pelo seu identificador.
     *
     * @param id identificador da categoria
     * @return categoria encontrada ou null caso não exista
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
    public CategoriaOcorrencia buscarPorId(Long id) {

        String sql = """
                SELECT id_categoria, nm_ocorrencia, ds_texto
                FROM CATEGORIA_OCORRENCIA
                WHERE id_categoria = ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar categoria.", e);
        }
    }

    /**
     * Cadastra uma nova categoria de ocorrência no banco de dados.
     * Após a inserção, recupera o identificador gerado pelo banco
     * e busca os dados cadastrados.
     *
     * @param x objeto contendo os dados da categoria que será cadastrada
     * @return categoria cadastrada com seu identificador preenchido
     * @throws RuntimeException caso ocorra um erro ao inserir os dados
     */
    public CategoriaOcorrencia cadastrar(CategoriaOcorrencia x) {

        String sql = """
                INSERT INTO CATEGORIA_OCORRENCIA (
                    nm_ocorrencia, ds_texto
                ) VALUES (?, ?)
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     sql, new String[]{"ID_CATEGORIA"})) {

            preencher(ps, x);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    x.setId(rs.getLong(1));
                }
            }

            return buscarPorId(x.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar categoria.", e);
        }
    }

    /**
     * Atualiza os dados de uma categoria de ocorrência existente.
     *
     * @param id identificador da categoria que será atualizada
     * @param x objeto contendo os novos dados da categoria
     * @return categoria atualizada ou null caso não seja encontrada
     * @throws RuntimeException caso ocorra um erro ao atualizar os dados
     */
    public CategoriaOcorrencia atualizar(Long id, CategoriaOcorrencia x) {

        String sql = """
                UPDATE CATEGORIA_OCORRENCIA
                SET nm_ocorrencia = ?, ds_texto = ?
                WHERE id_categoria = ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            preencher(ps, x);
            ps.setLong(3, id);

            if (ps.executeUpdate() == 0) {
                return null;
            }

            return buscarPorId(id);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria.", e);
        }
    }

    /**
     * Remove uma categoria de ocorrência do banco de dados pelo seu ID.
     *
     * @param id identificador da categoria que será removida
     * @return true se a categoria foi removida, false caso não tenha sido encontrada
     * @throws RuntimeException caso ocorra um erro ao excluir os dados
     */
    public boolean deletar(Long id) {

        String sql = """
                DELETE FROM CATEGORIA_OCORRENCIA
                WHERE id_categoria = ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar categoria.", e);
        }
    }

    /**
     * Verifica se já existe uma categoria com o nome informado.
     * A comparação desconsidera diferenças entre letras maiúsculas,
     * minúsculas e espaços nas extremidades.
     *
     * @param nome nome da categoria que será verificado
     * @return true se já existir uma categoria com esse nome, false caso contrário
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
    public boolean existePorNome(String nome) {

        String sql = """
            SELECT COUNT(*)
            FROM CATEGORIA_OCORRENCIA
            WHERE UPPER(TRIM(nm_ocorrencia)) = UPPER(TRIM(?))
            """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar categoria.", e);
        }
    }

    /**
     * Verifica se outra categoria, diferente da informada,
     * já utiliza o nome recebido.
     *
     * @param nome nome da categoria que será verificado
     * @param id identificador da categoria que deve ser ignorada na comparação
     * @return true se outra categoria já utilizar esse nome, false caso contrário
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
    public boolean existePorNomeEmOutraCategoria(String nome, Long id) {

        String sql = """
            SELECT COUNT(*)
            FROM CATEGORIA_OCORRENCIA
            WHERE UPPER(TRIM(nm_ocorrencia)) = UPPER(TRIM(?))
            AND id_categoria <> ?
            """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nome);
            ps.setLong(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar categoria.", e);
        }
    }

    /**
     * Preenche os parâmetros do PreparedStatement com os dados da categoria.
     *
     * @param ps comando SQL que receberá os valores
     * @param x objeto contendo os dados da categoria
     * @throws SQLException caso ocorra um erro ao preencher os parâmetros
     */
    private void preencher(PreparedStatement ps, CategoriaOcorrencia x)
            throws SQLException {

        ps.setString(1, x.getNomeOcorrencia());
        ps.setString(2, x.getDescTexto());
    }

    /**
     * Converte os dados de uma linha do ResultSet em um objeto
     * CategoriaOcorrencia.
     *
     * @param rs resultado da consulta SQL
     * @return objeto CategoriaOcorrencia preenchido com os dados da linha consultada
     * @throws SQLException caso ocorra um erro ao acessar os dados do resultado
     */
    private CategoriaOcorrencia mapear(ResultSet rs) throws SQLException {

        return new CategoriaOcorrencia(
                rs.getLong("id_categoria"),
                rs.getString("nm_ocorrencia"),
                rs.getString("ds_texto")
        );
    }
}
