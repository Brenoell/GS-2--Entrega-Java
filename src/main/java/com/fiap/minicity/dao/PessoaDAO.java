
package com.fiap.minicity.dao;

import com.fiap.minicity.exception.ConflitoException;
import com.fiap.minicity.models.Pessoa;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PessoaDAO {

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

    // Busca uma pessoa pelo e-mail, incluindo o hash da senha.
    // Será utilizado no login.
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

    public boolean deletar(Long id) {
        String sql = "DELETE FROM PESSOA WHERE id_pessoa = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        }  catch (SQLException e) {

        if (e.getErrorCode() == 2292) {
            throw new ConflitoException(
                    "Não é possível excluir esta pessoa, pois ela está vinculada a uma ou mais ocorrências."
            );
        }

        throw new RuntimeException("Erro ao deletar pessoa.", e);
    }
    }

    // Verifica se o e-mail já está cadastrado
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

    // Verifica se outra pessoa já utiliza esse e-mail
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

    // Transforma o resultado do banco em Pessoa, sem senha.
    private Pessoa mapear(ResultSet rs) throws SQLException {
        Pessoa pessoa = new Pessoa();

        pessoa.setId(rs.getLong("id_pessoa"));
        pessoa.setNome(rs.getString("nm_pessoa"));
        pessoa.setEmail(rs.getString("ds_email"));
        pessoa.setPerfil(rs.getString("ds_perfil"));
        pessoa.setDataCadastro(DAOUtil.data(rs, "dt_cadastro"));

        return pessoa;
    }

    // Transforma o resultado do banco em Pessoa, incluindo a senha.
    private Pessoa mapearComSenha(ResultSet rs) throws SQLException {
        Pessoa pessoa = mapear(rs);

        pessoa.setSenha(rs.getString("ds_senha"));

        return pessoa;
    }
}
