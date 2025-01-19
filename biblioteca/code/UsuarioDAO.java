import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void adicionarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, cpf, tipo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getTipo());
            stmt.executeUpdate();
        }
    }

    public Usuario buscarUsuarioPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("tipo")
                    );
                }
            }
        }
        return null;
    }

    public ArrayList<Usuario> listarUsuarios() throws SQLException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("tipo")
                ));
            }
        }
        return usuarios;
    }
}