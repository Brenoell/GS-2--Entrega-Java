
package com.fiap.minicity.dao;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Classe utilitária que reúne métodos auxiliares para operações
 * com o banco de dados, como fechamento de conexões e conversão de dados.
 */
public class DAOUtil {

    /**
     * Fecha uma conexão com o banco de dados, caso ela não seja nula.
     *
     * @param conexao conexão com o banco de dados que será fechada
     * @throws RuntimeException caso ocorra um erro ao fechar a conexão
     */
    public static void fechar(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Recupera uma data do ResultSet e converte para LocalDateTime.
     *
     * @param rs resultado da consulta SQL
     * @param coluna nome da coluna que contém a data
     * @return data convertida para LocalDateTime ou null caso não exista valor
     * @throws SQLException caso ocorra um erro ao acessar a coluna
     */
    public static LocalDateTime data(ResultSet rs, String coluna) throws SQLException {
        Timestamp valor = rs.getTimestamp(coluna);
        return valor == null ? null : valor.toLocalDateTime();
    }

    /**
     * Preenche um parâmetro do PreparedStatement com uma data.
     * Caso o valor seja nulo, define o parâmetro como TIMESTAMP nulo.
     *
     * @param ps comando SQL que receberá o valor
     * @param indice posição do parâmetro no comando SQL
     * @param valor data que será preenchida no comando
     * @throws SQLException caso ocorra um erro ao definir o parâmetro
     */
    public static void data(PreparedStatement ps, int indice, LocalDateTime valor) throws SQLException {
        if (valor == null) {
            ps.setNull(indice, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(indice, Timestamp.valueOf(valor));
        }
    }

    /**
     * Recupera o identificador de uma coluna do ResultSet.
     * Caso o valor da coluna seja nulo no banco, retorna null
     * em vez de retornar zero.
     *
     * @param rs resultado da consulta SQL
     * @param coluna nome da coluna que contém o identificador
     * @return identificador encontrado ou null caso o valor seja nulo
     * @throws SQLException caso ocorra um erro ao acessar a coluna
     */
    public static Long id(ResultSet rs, String coluna) throws SQLException {
        long valor = rs.getLong(coluna);
        return rs.wasNull() ? null : valor;
    }
}
