
package com.fiap.minicity.dao;

import com.fiap.minicity.exception.ConflitoException;
import com.fiap.minicity.models.Pessoa;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por realizar as operações de acesso ao banco de dados
 * relacionadas à entidade Pessoa.
 */
@Repository
public class PessoaDAO {

    /**
     * Lista todas as pessoas cadastradas no banco de dados.
     *
     * @return lista contendo as pessoas encontradas
     * @throws RuntimeException caso ocorra um erro na consulta ao banco
     */
    public List<Pessoa> listar() {
        List<Pessoa> lista = new ArrayList<>();

        String sql = """
                SELECT id_pessoa, nm_pessoa, ds_email, ds_perfil, dt_cadastro
                FROM PESSOA
                ORDER BY id_pessoa
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pessoas.", e);
        }
    }

    /**
     * Busca uma pessoa pelo seu identificador.
     *
     * @param id identificador da pessoa que será buscada
     * @return pessoa encontrada ou null caso não exista
     * @throws RuntimeException caso ocorra um erro na consulta ao banco
     */
    public Pessoa buscarPorId(Long id) {
        String sql = """
                SELECT id_pessoa, nm_pessoa, ds_email, ds_perfil, dt_cadastro
                FROM PESSOA
                WHERE id_pessoa = ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoa.", e);
        }
    }

    /**
     * Busca uma pessoa pelo e-mail, incluindo o hash da senha.
     * Esse método é utilizado no processo de login.
     *
     * @param email e-mail utilizado para localizar a pessoa
     * @return pessoa encontrada com a senha ou null caso não exista
     * @throws RuntimeException caso ocorra um erro na consulta ao banco
     */
    public Pessoa buscarPorEmail(String email) {
        String sql = """
                SELECT id_pessoa, nm_pessoa, ds_senha, ds_email, ds_perfil, dt_cadastro
                FROM PESSOA
                WHERE ds_email = ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapearComSenha(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoa pelo e-mail.", e);
        }
    }

    /**
     * Cadastra uma nova pessoa no banco de dados.
     *
     * @param x objeto Pessoa contendo os dados para cadastro
     * @return pessoa cadastrada com seu identificador preenchido e sem a senha
     * @throws RuntimeException caso ocorra um erro durante o cadastro
     */
    public Pessoa cadastrar(Pessoa x) {
        String sql = """
                INSERT INTO PESSOA (
                    nm_pessoa, ds_senha, ds_email, ds_perfil
                ) VALUES (?, ?, ?, ?)
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     sql, new String[]{"ID_PESSOA"})) {

            ps.setString(1, x.getNome());
            ps.setString(2, x.getSenha());
            ps.setString(3, x.getEmail());
            ps.setString(4, x.getPerfil() == null ? "MORADOR" : x.getPerfil());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    x.setId(rs.getLong(1));
                }
            }

            if (x.getPerfil() == null) {
                x.setPerfil("MORADOR");
            }

            // Não devolve a senha no objeto de resposta.
            x.setSenha(null);

            return x;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar pessoa.", e);
        }
    }

    /**
     * Atualiza os dados de uma pessoa no banco de dados.
     *
     * @param id identificador da pessoa que será atualizada
     * @param x objeto Pessoa contendo os novos dados
     * @return pessoa atualizada ou null caso não seja encontrada
     * @throws RuntimeException caso ocorra um erro durante a atualização
     */
    public Pessoa atualizar(Long id, Pessoa x) {
        String sql = """
                UPDATE PESSOA
                SET nm_pessoa = ?, ds_email = ?, ds_perfil = ?
                WHERE id_pessoa = ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, x.getNome());
            ps.setString(2, x.getEmail());
            ps.setString(3, x.getPerfil());
            ps.setLong(4, id);

            if (ps.executeUpdate() == 0) {
                return null;
            }

            x.setId(id);
            x.setSenha(null);

            return x;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pessoa.", e);
        }
    }

    /**
     * Exclui uma pessoa do banco de dados pelo seu identificador.
     *
     * @param id identificador da pessoa que será excluída
     * @return true se a pessoa foi excluída ou false caso não exista
     * @throws ConflitoException caso a pessoa esteja vinculada a ocorrências
     * @throws RuntimeException caso ocorra outro erro durante a exclusão
     */
    public boolean deletar(Long id) {
        String sql = "DELETE FROM PESSOA WHERE id_pessoa = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            if (e.getErrorCode() == 2292) {
                throw new ConflitoException(
                        "Não é possível excluir esta pessoa, pois ela está vinculada a uma ou mais ocorrências."
                );
            }

            throw new RuntimeException("Erro ao deletar pessoa.", e);
        }
    }

    /**
     * Verifica se um e-mail já está cadastrado no banco de dados.
     *
     * @param email e-mail que será verificado
     * @return true se o e-mail já existir ou false caso contrário
     * @throws RuntimeException caso ocorra um erro na consulta ao banco
     */
    public boolean existePorEmail(String email) {

        String sql = """
                SELECT COUNT(*)
                FROM PESSOA
                WHERE UPPER(TRIM(ds_email)) = UPPER(TRIM(?))
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar e-mail.", e);
        }
    }

    /**
     * Verifica se outra pessoa, diferente da informada, já utiliza o e-mail.
     *
     * @param email e-mail que será verificado
     * @param id identificador da pessoa que deve ser desconsiderada
     * @return true se outra pessoa utilizar o e-mail ou false caso contrário
     * @throws RuntimeException caso ocorra um erro na consulta ao banco
     */
    public boolean existePorEmailEmOutraPessoa(String email, Long id) {

        String sql = """
                SELECT COUNT(*)
                FROM PESSOA
                WHERE UPPER(TRIM(ds_email)) = UPPER(TRIM(?))
                AND id_pessoa <> ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setLong(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar e-mail.", e);
        }
    }

    /**
     * Converte os dados de um ResultSet em um objeto Pessoa,
     * sem incluir a senha.
     *
     * @param rs resultado da consulta ao banco de dados
     * @return objeto Pessoa preenchido com os dados encontrados
     * @throws SQLException caso ocorra um erro ao acessar os dados do resultado
     */
    private Pessoa mapear(ResultSet rs) throws SQLException {
        Pessoa pessoa = new Pessoa();

        pessoa.setId(rs.getLong("id_pessoa"));
        pessoa.setNome(rs.getString("nm_pessoa"));
        pessoa.setEmail(rs.getString("ds_email"));
        pessoa.setPerfil(rs.getString("ds_perfil"));
        pessoa.setDataCadastro(DAOUtil.data(rs, "dt_cadastro"));

        return pessoa;
    }

    /**
     * Converte os dados de um ResultSet em um objeto Pessoa,
     * incluindo o hash da senha.
     *
     * @param rs resultado da consulta ao banco de dados
     * @return objeto Pessoa preenchido com os dados, incluindo a senha
     * @throws SQLException caso ocorra um erro ao acessar os dados do resultado
     */
    private Pessoa mapearComSenha(ResultSet rs) throws SQLException {
        Pessoa pessoa = mapear(rs);

        pessoa.setSenha(rs.getString("ds_senha"));

        return pessoa;
    }
}
