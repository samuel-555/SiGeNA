package sigena.model.domain;

public class Usuario {

    private int id;
    private String cpf;
    private String senha;
    private Cargo cargo;

    public Usuario() {
    }

    public Usuario(String cpf, String senha, Cargo cargo) {
        this.cpf = cpf;
        this.senha = senha;
        this.cargo = cargo;
    }
    public Usuario(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}
