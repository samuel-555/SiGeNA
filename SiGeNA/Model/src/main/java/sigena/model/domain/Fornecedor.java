package sigena.model.domain;
import sigena.model.domain.util.FornecedorTipo;

public class Fornecedor {
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private FornecedorTipo tipo;
    
    public Fornecedor(String nome, String telefone, String email, String endereco, String tipo) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.tipo = FornecedorTipo.setTipo(tipo);
    }
    
    public Fornecedor(Long id, String nome, String telefone, String email, String endereco, String tipo) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.tipo = FornecedorTipo.setTipo(tipo);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTipo() {
        return tipo.getTipo();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTipo(String tipo) {
        this.tipo = FornecedorTipo.setTipo(tipo);
    }
}
