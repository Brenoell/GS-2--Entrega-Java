
package com.fiap.minicity.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por criar e fornecer conexões
 * com o banco de dados Oracle.
 */
public class ConnectionFactory {

    /**
     * Obtém uma conexão com o banco de dados Oracle.
     * Utiliza as variáveis de ambiente para recuperar a URL,
     * o usuário e a senha de acesso ao banco.
     *
     * @return conexão estabelecida com o banco de dados
     * @throws IllegalStateException caso as variáveis de ambiente
     *         não estejam configuradas
     * @throws RuntimeException caso ocorra um erro ao estabelecer a conexão
     */
    public static Connection obterConexao() {
        String url = System.getenv("SPRING_DATASOURCE_URL");
        String usuario = System.getenv("SPRING_DATASOURCE_USERNAME");
        String senha = System.getenv("SPRING_DATASOURCE_PASSWORD");

        if (url == null || usuario == null || senha == null) {
            throw new IllegalStateException(
                    "Variáveis de ambiente do Oracle não configuradas."
            );
        }

        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao conectar ao banco Oracle.", e
            );
        }
    }
}
