import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    private final Connection connection;

    public LivroDAO(Connection connection) {
        this.connection = connection;
    }

    // Adicionar um livro ao banco de dados
    public void adicionarLivro(Livro livro) throws SQLException {
        String sql = "INSERT INTO livros (titulo, autor, disponivel) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setBoolean(3, livro.isDisponivel());
            stmt.executeUpdate();
        }
    }

    // Remover um livro do banco de dados pelo ID
    public void removerLivro(int id) throws SQLException {
        String sql = "DELETE FROM livros WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Listar todos os livros disponíveis
    public List<Livro> listarLivrosDisponiveis() throws SQLException {
        String sql = "SELECT * FROM livros WHERE disponivel = TRUE";
        List<Livro> livros = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getBoolean("disponivel")
                );
                livros.add(livro);
            }
        }
        return livros;
    }

    // Listar todos os livros (disponíveis e indisponíveis)
    public List<Livro> listarTodosOsLivros() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros"; // Consulta sem filtro
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getBoolean("disponivel")
                );
                livros.add(livro);
            }
        }
        return livros;
    }
    

    // Atualizar a disponibilidade de um livro no banco de dados
    public void atualizarDisponibilidade(int id, boolean disponivel) throws SQLException {
        String sql = "UPDATE livros SET disponivel = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, disponivel);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
    // Método para buscar um livro por ID
    public Livro buscarLivroPorId(int id) throws SQLException {
        String sql = "SELECT * FROM livros WHERE id = ?";
        Livro livro = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String titulo = resultSet.getString("titulo");
                    String autor = resultSet.getString("autor");
                    boolean disponivel = resultSet.getBoolean("disponivel");

                    livro = new Livro(id, titulo, autor, disponivel);
                }
            }
        }
        return livro;
    }
}
