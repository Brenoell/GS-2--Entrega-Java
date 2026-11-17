
package com.fiap.minicity.dao;

import com.fiap.minicity.models.Comentario;
import com.fiap.minicity.models.Pessoa;
import com.fiap.minicity.models.Distrito;
import com.fiap.minicity.models.Ocorrencia;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar as operações de acesso ao banco de dados
 * relacionadas aos comentários.
 */
@Repository
public class ComentarioDAO {

    /**
     * Lista todos os comentários cadastrados no banco de dados,
     * incluindo os dados da pessoa e do distrito relacionados.
     *
     * @return lista contendo todos os comentários
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
    public List<Comentario> listar() {
        List<Comentario> lista = new ArrayList<>();

        String sql = """
                SELECT
                    c.id_comentario,
                    c.ds_conteudo,
                    c.dt_publicacao,

                    p.id_pessoa,
                    p.nm_nome,
                    p.ds_email,
                    p.ds_perfil,
                    p.dt_cadastro,

                    d.id_distrito,
                    d.nm_distrito,
                    d.ds_zona,
                    d.ds_latitude,
                    d.ds_longitude,

                    c.id_ocorrencia

                FROM COMENTARIO c

                JOIN PESSOA p
                    ON c.id_pessoa = p.id_pessoa

                JOIN DISTRITO d
                    ON c.id_distrito = d.id_distrito

                ORDER BY c.dt_publicacao DESC
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar comentários.", e);
        }
    }

    /**
     * Lista os comentários vinculados a uma ocorrência específica,
     * organizando os resultados pela data de publicação, do mais recente
     * para o mais antigo.
     *
     * @param idOcorrencia identificador da ocorrência
     * @return lista de comentários vinculados à ocorrência informada
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
    public List<Comentario> listarPorOcorrencia(Long idOcorrencia) {
        List<Comentario> lista = new ArrayList<>();

        String sql = """
                SELECT
                    c.id_comentario,
                    c.ds_conteudo,
                    c.dt_publicacao,

                    p.id_pessoa,
                    p.nm_nome,
                    p.ds_email,
                    p.ds_perfil,
                    p.dt_cadastro,

                    d.id_distrito,
                    d.nm_distrito,
                    d.ds_zona,
                    d.ds_latitude,
                    d.ds_longitude,

                    c.id_ocorrencia

                FROM COMENTARIO c

                JOIN PESSOA p
                    ON c.id_pessoa = p.id_pessoa

                JOIN DISTRITO d
                    ON c.id_distrito = d.id_distrito

                WHERE c.id_ocorrencia = ?

                ORDER BY c.dt_publicacao DESC
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, idOcorrencia);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar comentários da ocorrência.", e);
        }
    }

    /**
     * Busca um comentário pelo seu identificador.
     *
     * @param id identificador do comentário
     * @return comentário encontrado ou null caso não exista
     * @throws RuntimeException caso ocorra um erro ao consultar o banco
     */
    public Comentario buscarPorId(Long id) {
        String sql = """
                SELECT
                    c.id_comentario,
                    c.ds_conteudo,
                    c.dt_publicacao,

                    p.id_pessoa,
                    p.nm_nome,
                    p.ds_email,
                    p.ds_perfil,
                    p.dt_cadastro,

                    d.id_distrito,
                    d.nm_distrito,
                    d.ds_zona,
                    d.ds_latitude,
                    d.ds_longitude,

                    c.id_ocorrencia

                FROM COMENTARIO c

                JOIN PESSOA p
                    ON c.id_pessoa = p.id_pessoa

                JOIN DISTRITO d
                    ON c.id_distrito = d.id_distrito

                WHERE c.id_comentario = ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar comentário.", e);
        }

        return null;
    }

    /**
     * Cadastra um novo comentário no banco de dados.
     * Após a inserção, recupera o identificador gerado pelo banco.
     *
     * @param comentario objeto contendo os dados do comentário que será cadastrado
     * @return objeto do comentário cadastrado com seu identificador preenchido
     * @throws RuntimeException caso ocorra um erro ao inserir os dados
     */
    public Comentario cadastrar(Comentario comentario) {
        String sql = """
                INSERT INTO COMENTARIO
                (ds_conteudo, id_pessoa, id_distrito, id_ocorrencia)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     sql, new String[]{"ID_COMENTARIO"})) {

            preencher(ps, comentario);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    comentario.setId(rs.getLong(1));
                }
            }

            return comentario;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar comentário.", e);
        }
    }

    /**
     * Atualiza os dados de um comentário existente no banco de dados.
     *
     * @param id identificador do comentário que será atualizado
     * @param comentario objeto contendo os novos dados do comentário
     * @return objeto atualizado ou null caso o comentário não seja encontrado
     * @throws RuntimeException caso ocorra um erro ao atualizar os dados
     */
    public Comentario atualizar(Long id, Comentario comentario) {
        String sql = """
                UPDATE COMENTARIO
                SET
                    ds_conteudo = ?,
                    id_pessoa = ?,
                    id_distrito = ?,
                    id_ocorrencia = ?
                WHERE id_comentario = ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            preencher(ps, comentario);
            ps.setLong(5, id);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                comentario.setId(id);
                return comentario;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar comentário.", e);
        }

        return null;
    }

    /**
     * Remove um comentário do banco de dados pelo seu identificador.
     *
     * @param id identificador do comentário que será removido
     * @return true se o comentário foi removido, false caso não tenha sido encontrado
     * @throws RuntimeException caso ocorra um erro ao excluir os dados
     */
    public boolean deletar(Long id) {
        String sql = "DELETE FROM COMENTARIO WHERE id_comentario = ?";

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar comentário.", e);
        }
    }

    /**
     * Preenche os parâmetros do PreparedStatement com os dados do comentário.
     * Também verifica se a ocorrência possui identificador para definir
     * o valor correspondente no comando SQL.
     *
     * @param ps comando SQL que receberá os valores
     * @param comentario objeto contendo os dados do comentário
     * @throws SQLException caso ocorra um erro ao preencher os parâmetros
     */
    private void preencher(
            PreparedStatement ps,
            Comentario comentario
    ) throws SQLException {

        ps.setString(1, comentario.getDescConteudo());
        ps.setLong(2, comentario.getPessoa().getId());
        ps.setLong(3, comentario.getDistrito().getId());

        if (comentario.getOcorrencia() != null
                && comentario.getOcorrencia().getId() != null) {

            ps.setLong(4, comentario.getOcorrencia().getId());

        } else {
            ps.setNull(4, Types.NUMERIC);
        }
    }

    /**
     * Converte os dados de uma linha do ResultSet em um objeto Comentario.
     * Também cria e preenche os objetos relacionados Pessoa, Distrito
     * e Ocorrencia.
     *
     * @param rs resultado da consulta SQL
     * @return objeto Comentario preenchido com os dados da linha consultada
     * @throws SQLException caso ocorra um erro ao acessar os dados do resultado
     */
    private Comentario mapear(ResultSet rs) throws SQLException {

        Pessoa pessoa = new Pessoa();

        pessoa.setId(rs.getLong("id_pessoa"));
        pessoa.setNome(rs.getString("nm_nome"));
        pessoa.setEmail(rs.getString("ds_email"));
        pessoa.setPerfil(rs.getString("ds_perfil"));

        Timestamp dataCadastro = rs.getTimestamp("dt_cadastro");

        if (dataCadastro != null) {
            pessoa.setDataCadastro(dataCadastro.toLocalDateTime());
        }

        Distrito distrito = new Distrito(
                rs.getLong("id_distrito"),
                rs.getString("nm_distrito"),
                rs.getString("ds_zona"),
                rs.getDouble("ds_latitude"),
                rs.getDouble("ds_longitude")
        );

        Ocorrencia ocorrencia = null;

        long idOcorrencia = rs.getLong("id_ocorrencia");

        if (!rs.wasNull()) {
            ocorrencia = new Ocorrencia();
            ocorrencia.setId(idOcorrencia);
        }

        Comentario comentario = new Comentario();

        comentario.setId(rs.getLong("id_comentario"));
        comentario.setDescConteudo(rs.getString("ds_conteudo"));

        Timestamp data = rs.getTimestamp("dt_publicacao");

        if (data != null) {
            comentario.setDataPublicacao(data.toLocalDateTime());
        }

        comentario.setPessoa(pessoa);
        comentario.setDistrito(distrito);
        comentario.setOcorrencia(ocorrencia);

        return comentario;
    }
}
