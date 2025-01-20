import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Connection connection = Conector.getConnection();
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            LivroDAO livroDAO = new LivroDAO(connection);

            Usuario usuarioLogado = null;

            while (true) {
                if (usuarioLogado == null) {
                    usuarioLogado = menuInicial(scanner, usuarioDAO);
                } else {
                    menuUsuarioLogado(scanner, usuarioLogado, livroDAO);
                    usuarioLogado = null; // Após o logoff, volta para o menu inicial
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
    }

    private static Usuario menuInicial(Scanner scanner, UsuarioDAO usuarioDAO) {
        while (true) {
            System.out.println("1. Logar");
            System.out.println("2. Cadastrar");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    return logarUsuario(scanner, usuarioDAO);
                case 2:
                    cadastrarUsuario(scanner, usuarioDAO);
                    break;
                case 3:
                    System.out.println("Saindo...");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static Usuario logarUsuario(Scanner scanner, UsuarioDAO usuarioDAO) {
        System.out.print("Digite seu CPF: ");
        String cpf = scanner.nextLine();
        try {
            Usuario usuario = usuarioDAO.buscarUsuarioPorCpf(cpf);
            if (usuario != null) {
                System.out.println("Login bem-sucedido!");
                return usuario;
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }

    private static void cadastrarUsuario(Scanner scanner, UsuarioDAO usuarioDAO) {
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite seu CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Digite seu tipo de usuário (aluno/professor): ");
        String tipoUsuario = scanner.nextLine();

        Usuario novoUsuario = new Usuario(nome, cpf, tipoUsuario);
        try {
            usuarioDAO.adicionarUsuario(novoUsuario);
            System.out.println("Cadastro realizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    private static void menuUsuarioLogado(Scanner scanner, Usuario usuarioLogado, LivroDAO livroDAO) {
        while (true) {
            System.out.println("Bem-vindo, " + usuarioLogado.getNome());
            System.out.println("1. Alugar Livro");
            System.out.println("2. Devolver Livro");
            System.out.println("3. Adicionar Livro");
            System.out.println("4. Remover Livro");
            System.out.println("5. Listar Livros");
            System.out.println("6. Logoff");
            System.out.print("Escolha uma opção: ");
    
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
    
            switch (opcao) {
                case 1:
                    alugarLivro(scanner, usuarioLogado, livroDAO);
                    break;
                    case 2:
                    if (usuarioLogado.getLivrosAlugados().isEmpty()) {
                        System.out.println("Você não tem livros para devolver.");
                    } else {
                        System.out.println("Livros alugados:");
                        List<Livro> livrosAlugados = usuarioLogado.getLivrosAlugados();
                        for (int i = 0; i < livrosAlugados.size(); i++) {
                            System.out.println((i + 1) + ". " + livrosAlugados.get(i).getTitulo());
                        }
                        System.out.print("Escolha o número do livro para devolver: ");
                        int escolhaDevolucao = scanner.nextInt();
                        scanner.nextLine(); // Consumir a quebra de linha
                
                        if (escolhaDevolucao >= 1 && escolhaDevolucao <= livrosAlugados.size()) {
                            Livro livroParaDevolver = livrosAlugados.get(escolhaDevolucao - 1);
                            usuarioLogado.devolverLivro(livroParaDevolver, livroDAO);
                        } else {
                            System.out.println("Opção inválida.");
                        }
                    }
                    break;
                case 3:
                    adicionarLivro(scanner, livroDAO);
                    break;
                case 4:
                    removerLivro(scanner, livroDAO);
                    break;
                case 5:
                    listarLivros(livroDAO);
                    break;
                case 6:
                    System.out.println("Fazendo logoff...");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void listarLivros(LivroDAO livroDAO) {
        try {
            List<Livro> livros = livroDAO.listarTodosOsLivros(); // Alterar para listar todos os livros
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro encontrado.");
            } else {
                System.out.println("Lista de Livros:");
                for (Livro livro : livros) {
                    System.out.println(
                        "ID: " + livro.getId() +
                        " | Título: " + livro.getTitulo() +
                        " | Autor: " + livro.getAutor() +
                        " | Disponível: " + (livro.isDisponivel() ? "Sim" : "Não")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
        }
    }

    private static void alugarLivro(Scanner scanner, Usuario usuarioLogado, LivroDAO livroDAO) {
        try {
            List<Livro> livrosDisponiveis = livroDAO.listarLivrosDisponiveis();
            if (livrosDisponiveis.isEmpty()) {
                System.out.println("Não há livros disponíveis para aluguel.");
                return;
            }
    
            System.out.println("Livros disponíveis para aluguel:");
            for (int i = 0; i < livrosDisponiveis.size(); i++) {
                System.out.println((i + 1) + ". " + livrosDisponiveis.get(i).getTitulo());
            }
    
            System.out.print("Escolha o número do livro para alugar: ");
            int escolha = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
    
            if (escolha >= 1 && escolha <= livrosDisponiveis.size()) {
                Livro livroSelecionado = livrosDisponiveis.get(escolha - 1);
                usuarioLogado.alugarLivro(livroSelecionado, livroDAO);
            } else {
                System.out.println("Opção inválida.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar ou alugar livro: " + e.getMessage());
        }
    }
    
    
    
    // add livro.
    private static void adicionarLivro(Scanner scanner, LivroDAO livroDAO) {
        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine();
    
        System.out.print("Digite o autor do livro: ");
        String autor = scanner.nextLine();
    
        Livro novoLivro = new Livro(0, titulo, autor, true); // `0` será gerado automaticamente pelo banco.
        try {
            livroDAO.adicionarLivro(novoLivro);
            System.out.println("Livro adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar livro: " + e.getMessage());
        }
    }

    //remover livro
    private static void removerLivro(Scanner scanner, LivroDAO livroDAO) {
        try {
            List<Livro> livros = livroDAO.listarLivrosDisponiveis();
            if (livros.isEmpty()) {
                System.out.println("Não há livros disponíveis para remover.");
                return;
            }
    
            System.out.println("Livros disponíveis:");
            for (int i = 0; i < livros.size(); i++) {
                System.out.println((i + 1) + ". " + livros.get(i).getTitulo());
            }
    
            System.out.print("Escolha o número do livro para remover: ");
            int escolha = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
    
            if (escolha >= 1 && escolha <= livros.size()) {
                Livro livroSelecionado = livros.get(escolha - 1);
                livroDAO.removerLivro(livroSelecionado.getId());
                System.out.println("Livro removido com sucesso!");
            } else {
                System.out.println("Opção inválida.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover livro: " + e.getMessage());
        }
    }
    
}

