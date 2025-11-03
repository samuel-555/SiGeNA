package sigena.model.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Funcionario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String cargo;
    private LocalDateTime dataCadastro;

    public Funcionario() {
        this.dataCadastro = LocalDateTime.now();
    }

    public Funcionario(String nome, String email, String senha, String cargo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.dataCadastro = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public String getDataCadastroFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataCadastro.format(formatter);
    }
}
