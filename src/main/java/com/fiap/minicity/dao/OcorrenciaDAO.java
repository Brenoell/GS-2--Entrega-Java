
package com.fiap.minicity.dao;

import com.fiap.minicity.models.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OcorrenciaDAO {

    private final String colunas = """
            SELECT id_ocorrencia, nm_titulo, ds_ocorrencia,
                   nr_gravidade, ds_origem, ds_status,
                   nr_latitude, nr_longitude,
                   dt_ocorrencia, dt_publicacao, dt_atualizacao,
                   id_distrito, id_categoria, id_fonte,
                   id_pessoa, id_externo
            FROM OCORRENCIA
            """;

    public List<Ocorrencia> listar() {

        List<Ocorrencia> lista = new ArrayList<>();

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     colunas + " ORDER BY id_ocorrencia");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar ocorrências.", e);
        }
    }

    public Ocorrencia buscarPorId(Long id) {

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     colunas + " WHERE id_ocorrencia = ?")) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ocorrência.", e);
        }
    }

    public Ocorrencia cadastrar(Ocorrencia x) {

        String sql = """
                INSERT INTO OCORRENCIA (
                    nm_titulo, ds_ocorrencia, nr_gravidade,
                    ds_origem, ds_status, nr_latitude, nr_longitude,
                    dt_ocorrencia, dt_atualizacao,
                    id_distrito, id_categoria, id_fonte,
                    id_pessoa, id_externo
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     sql, new String[]{"ID_OCORRENCIA"})) {

            preencher(ps, x);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    x.setId(rs.getLong(1));
                }
            }

            return buscarPorId(x.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar ocorrência.", e);
        }
    }

    public Ocorrencia atualizar(Long id, Ocorrencia x) {

        String sql = """
                UPDATE OCORRENCIA SET
                    nm_titulo = ?,
                    ds_ocorrencia = ?,
                    nr_gravidade = ?,
                    ds_origem = ?,
                    ds_status = ?,
                    nr_latitude = ?,
                    nr_longitude = ?,
                    dt_ocorrencia = ?,
                    dt_atualizacao = ?,
                    id_distrito = ?,
                    id_categoria = ?,
                    id_fonte = ?,
                    id_pessoa = ?,
                    id_externo = ?
                WHERE id_ocorrencia = ?
                """;

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {

            preencher(ps, x);
            ps.setLong(15, id);

            if (ps.executeUpdate() == 0) {
                return null;
            }

            return buscarPorId(id);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar ocorrência.", e);
        }
    }

    public boolean deletar(Long id) {

        try (Connection c = ConnectionFactory.obterConexao();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM OCORRENCIA WHERE id_ocorrencia = ?")) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar ocorrência.", e);
        }
    }

    private void preencher(PreparedStatement ps, Ocorrencia x)
            throws SQLException {

        ps.setString(1, x.getNomeTitulo());
        ps.setString(2, x.getDescOcorrencia());
        ps.setObject(3, x.getGravidade());
        ps.setString(4, x.getOrigem());

        ps.setString(5, x.getStatus() == null ? "ATIVA" : x.getStatus());

        ps.setObject(6, x.getLatitude());
        ps.setObject(7, x.getLongitude());

        DAOUtil.data(ps, 8, x.getDataOcorrencia());
        DAOUtil.data(ps, 9, x.getDataAtualizacao());

        ps.setLong(10, x.getDistrito().getId());
        ps.setLong(11, x.getCategoria().getId());

        if (x.getFonte() == null) {
            ps.setNull(12, Types.NUMERIC);
        } else {
            ps.setLong(12, x.getFonte().getId());
        }

        if (x.getPessoa() == null) {
            ps.setNull(13, Types.NUMERIC);
        } else {
            ps.setLong(13, x.getPessoa().getId());
        }

        ps.setString(14, x.getIdExterno());
    }

    private Ocorrencia mapear(ResultSet rs) throws SQLException {

        Ocorrencia x = new Ocorrencia();

        x.setId(rs.getLong("id_ocorrencia"));
        x.setNomeTitulo(rs.getString("nm_titulo"));
        x.setDescOcorrencia(rs.getString("ds_ocorrencia"));
        x.setGravidade(rs.getInt("nr_gravidade"));
        x.setOrigem(rs.getString("ds_origem"));
        x.setStatus(rs.getString("ds_status"));

        x.setLatitude(rs.getDouble("nr_latitude"));
        x.setLongitude(rs.getDouble("nr_longitude"));

        x.setDataOcorrencia(DAOUtil.data(rs, "dt_ocorrencia"));
        x.setDataPublicacao(DAOUtil.data(rs, "dt_publicacao"));
        x.setDataAtualizacao(DAOUtil.data(rs, "dt_atualizacao"));

        Distrito distrito = new Distrito();
        distrito.setId(rs.getLong("id_distrito"));
        x.setDistrito(distrito);

        CategoriaOcorrencia categoria = new CategoriaOcorrencia();
        categoria.setId(rs.getLong("id_categoria"));
        x.setCategoria(categoria);

        Long idFonte = DAOUtil.id(rs, "id_fonte");

        if (idFonte != null) {
            FonteDado fonte = new FonteDado();
            fonte.setId(idFonte);
            x.setFonte(fonte);
        }

        Long idPessoa = DAOUtil.id(rs, "id_pessoa");

        if (idPessoa != null) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(idPessoa);
            x.setPessoa(pessoa);
        }

        x.setIdExterno(rs.getString("id_externo"));

        return x;
    }
}
