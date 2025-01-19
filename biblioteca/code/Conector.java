import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conector {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Conexão com o banco de dados
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "Guil@2006");
                System.out.println("Conexão bem-sucedida com o banco de dados!");
            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Conexão fechada.");
        }
    }
}
