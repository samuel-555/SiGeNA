package sigena.model;

public class Usuario {
    private int id;
    private String cpf;
    private String senha;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}
