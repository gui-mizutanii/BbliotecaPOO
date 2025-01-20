# Relatório de Orientação a Objetos no Sistema de Biblioteca

## **Aplicação dos Conceitos de Orientação a Objetos (POO)**
O sistema de gestão de biblioteca foi projetado utilizando o paradigma da orientação a objetos (POO), distribuindo a responsabilidade em diversas classes e promoven reutilização de códigodo a, manutenção facilitada e modularidade. Abaixo, apresentamos como os princípios fundamentais de POO foram aplicados:

### **1. Encapsulamento**
Os atributos de cada classe foram definidos como `private`, garantindo que os dados só possam ser acessados e modificados através de métodos específicos (getters e setters). Isso protege os dados contra manipulações indevidas.

#### Exemplo de Encapsulamento:
Classe `Usuario`:
```java
public class Usuario {
    private String nome;
    private String cpf;
    private String tipo; // "aluno" ou "professor"

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
}
```
**Benefícios:** A manipulação de dados como `nome` e `cpf` é controlada, evitando inconsistências ou acessos indevidos.

---

### **2. Herança**
Embora o código atual não implemente diretamente herança, ele está estruturado para permitir a expansão. Por exemplo, a classe `Usuario` poderia ser utilizada como superclasse para `Aluno` e `Professor`, que compartilham comportamentos comuns, mas com características específicas.

#### Exemplo de Implementação de Herança (Hipotético):
```java
public class Aluno extends Usuario {
    public Aluno(String nome, String cpf) {
        super(nome, cpf, "aluno"); // Chama o construtor da superclasse
    }
}

public class Professor extends Usuario {
    public Professor(String nome, String cpf) {
        super(nome, cpf, "professor");
    }
}
```
**Benefícios:** Evita repetição de código e permite que comportamentos comuns sejam mantidos em um único lugar.

---

### **3. Polimorfismo**
Polimorfismo é aplicado permitindo que diferentes tipos de usuários (aluno e professor) compartilhem o mesmo método, mas com comportamentos distintos. No método `alugarLivro()`, o limite de livros alugados é adaptado com base no tipo de usuário.

#### Exemplo de Polimorfismo:
```java
public void alugarLivro(Livro livro, LivroDAO livroDAO) {
    int limite = tipo.equalsIgnoreCase("aluno") ? 3 : 5; // Limite depende do tipo

    if (livrosAlugados.size() >= limite) {
        System.out.println("Limite de aluguel atingido. " + tipo + " pode alugar no máximo " + limite + " livros.");
        return;
    }

    if (livro.isDisponivel()) {
        try {
            livroDAO.atualizarDisponibilidade(livro.getId(), false);
            livrosAlugados.add(livro);
            livro.setDisponivel(false);
            System.out.println(nome + " alugou o livro: " + livro.getTitulo());
        } catch (SQLException e) {
            System.err.println("Erro ao registrar aluguel no banco de dados: " + e.getMessage());
        }
    } else {
        System.out.println("O livro " + livro.getTitulo() + " não está disponível.");
    }
}
```
**Benefícios:** Permite o uso de um método genérico para diferentes tipos de usuários, com adaptações específicas.

---

## **Padrões de Projeto Utilizados**

### **1. DAO (Data Access Object)**
- **Onde foi utilizado:** Classes `LivroDAO` e `UsuarioDAO`.
- **Motivo:** Este padrão foi usado para separar a lógica de acesso ao banco de dados da lógica de negócios do sistema. Isso melhora a manutenção e a testabilidade, além de permitir mudanças no banco de dados sem afetar outras partes do sistema.

#### Exemplo:
Classe `LivroDAO`:
```java
public class LivroDAO {
    private final Connection connection;

    public LivroDAO(Connection connection) {
        this.connection = connection;
    }

    public void adicionarLivro(Livro livro) throws SQLException {
        String sql = "INSERT INTO livros (titulo, autor, disponivel) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setBoolean(3, livro.isDisponivel());
            stmt.executeUpdate();
        }
    }
}
```
**Benefícios:** Garante que o acesso ao banco seja centralizado e reutilizável.

---

### **2. Singleton (Potencial para Conexão com Banco)**
- **Onde poderia ser utilizado:** Para gerenciar a conexão com o banco de dados, garantindo que apenas uma instância da conexão exista durante a execução.

#### Exemplo (Potencial):
```java
public class DatabaseConnection {
    private static Connection connection;

    public static Connection getInstance() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "user", "password");
        }
        return connection;
    }
}
```
**Benefícios:** Evita desperdício de recursos e facilita o gerenciamento da conexão.

---

## **Conclusão**

- Os maiores desafios do projeto foi organizar o código de um jeito que ele não ficasse de modo estruturado, deixando o código orientado a objetos, também tive muita dificuldade para fazer a interação do código com o banco de dados.
- O sistema utiliza os princípios da orientação a objetos para organizar a lógica do sistema, garantindo facilidade de manutenção e melhoramento. Usando padrões de projeto como a DAO e Singleton que fortalecem a arquitetura, promovendo separação de responsabilidades e eficiência no gerenciamento de recursos. 
