package fiap.tds.infraestructure;


import fiap.tds.exception.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "rm553326";
    private static final String PASSWORD = "091003";
    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class);

    /**
     * Método para obter uma conexão com o banco de dados.
     *
     * @return Connection objeto de conexão com o banco de dados.
     * @throws DatabaseException se ocorrer um erro ao conectar.
     */
    public static Connection getConnection() throws DatabaseException {
        try {
            logger.debug("Tentando conectar ao banco de dados...");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.debug("Conexão estabelecida com sucesso.");
            return connection;
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados: ", e);
            throw new DatabaseException("Erro ao conectar ao banco de dados", e);
        }
    }
}
