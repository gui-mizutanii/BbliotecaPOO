import java.sql.*;
import java.util.ArrayList;

public class Biblioteca {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Livro> livros;

    public Biblioteca() {
        livros = new ArrayList<Livro>();
        usuarios = new ArrayList<Usuario>();
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public void setLivros(ArrayList<Livro> livros) {
        this.livros = livros;
    }

    public void adicionarUsuario(Usuario usuario, Connection connection) throws SQLException {
        usuarios.add(usuario);

        // Inserir o usuário no banco de dados
        String sql = "INSERT INTO usuarios (nome, cpf, tipo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getTipo());
            stmt.executeUpdate();
        }

        System.out.println("Usuário cadastrado: " + usuario.getNome());
    }

    public Livro buscarLivro(String titulo) {
        for (Livro livro : livros) {
            if (livro.getTitulo().equals(titulo)) {
                return livro;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorCpf(String cpf) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                return usuario;
            }
        }
        return null;
    }

    public ArrayList<Livro> listarLivrosDisponiveis() {
        ArrayList<Livro> livrosDisponiveis = new ArrayList<>();
        for (Livro livro : livros) {
            if (livro.isDisponivel()) {
                livrosDisponiveis.add(livro);
            }
        }
        return livrosDisponiveis;
    }

    public void adicionarLivro(Livro livro, Connection connection) throws SQLException {
        livros.add(livro);
        
        // Inserir o livro no banco de dados
        String sql = "INSERT INTO livros (titulo, autor, disponivel) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setBoolean(3, livro.isDisponivel());
            stmt.executeUpdate();
        }
    
        System.out.println("Livro adicionado: " + livro.getTitulo());
    }
    
    public void removerLivro(int livroId, Connection connection) throws SQLException {
        // Remover o livro da lista local
        Livro livroARemover = buscarLivroPorId(livroId);
        if (livroARemover != null) {
            livros.remove(livroARemover);
            
            // Remover o livro do banco de dados
            String sql = "DELETE FROM livros WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, livroId);
                stmt.executeUpdate();
            }
    
            System.out.println("Livro removido: " + livroARemover.getTitulo());
        } else {
            System.out.println("Livro não encontrado.");
        }
    }
    
    public Livro buscarLivroPorId(int livroId) {
        for (Livro livro : livros) {
            if (livro.getId() == livroId) {
                return livro;
            }
        }
        return null;
    }
    
}