package com.fiap.minicity.dao;

import java.sql.*;
import java.time.LocalDateTime;

public class DAOUtil {

    public static void fechar(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static LocalDateTime data(ResultSet rs, String coluna) throws SQLException {
        Timestamp valor = rs.getTimestamp(coluna);
        return valor == null ? null : valor.toLocalDateTime();
    }

    public static void data(PreparedStatement ps, int indice, LocalDateTime valor) throws SQLException {
        if (valor == null) {
            ps.setNull(indice, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(indice, Timestamp.valueOf(valor));
        }
    }

    public static Long id(ResultSet rs, String coluna) throws SQLException {
        long valor = rs.getLong(coluna);
        return rs.wasNull() ? null : valor;
    }
}