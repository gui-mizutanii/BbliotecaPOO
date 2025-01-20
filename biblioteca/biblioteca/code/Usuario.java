import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private String cpf;
    private String tipo;
    private List<Livro> livrosAlugados;

    public Usuario(String nome, String cpf, String tipo) {
        this.nome = nome;
        this.cpf = cpf;
        this.tipo = tipo;
        this.livrosAlugados = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Livro> getLivrosAlugados() {
        return livrosAlugados;
    }

    public void alugarLivro(Livro livro, LivroDAO livroDAO) {
        int limite = tipo.equalsIgnoreCase("aluno") ? 3 : 5;

        if (livrosAlugados.size() >= limite) {
            System.out.println("Limite de aluguel atingido.");
            return;
        }

        if (livro.isDisponivel()) {
            try {
                livroDAO.atualizarDisponibilidade(livro.getId(), false);
                livrosAlugados.add(livro);
                livro.setDisponivel(false);
                System.out.println(nome + " alugou o livro: " + livro.getTitulo());
            } catch (SQLException e) {
                System.err.println("Erro ao registrar aluguel: " + e.getMessage());
            }
        } else {
            System.out.println("O livro " + livro.getTitulo() + " não está disponível.");
        }
    }

    public void devolverLivro(Livro livro, LivroDAO livroDAO) {
        if (livrosAlugados.contains(livro)) {
            try {
                livroDAO.atualizarDisponibilidade(livro.getId(), true);
                livrosAlugados.remove(livro);
                livro.setDisponivel(true);
                System.out.println(nome + " devolveu o livro: " + livro.getTitulo());
            } catch (SQLException e) {
                System.err.println("Erro ao registrar devolução: " + e.getMessage());
            }
        } else {
            System.out.println(nome + " não alugou o livro: " + livro.getTitulo());
        }
    }

    public void listarLivrosAlugados() {
        if (livrosAlugados.isEmpty()) {
            System.out.println(nome + " não tem livros alugados.");
        } else {
            System.out.println("Livros alugados por " + nome + ":");
            for (Livro livro : livrosAlugados) {
                System.out.println("- " + livro.getTitulo());
            }
        }
    }
}
