package sigena.model.domain;

public class Funcionario {

    private int id;
    private String nome;
    private Cargo cargo;
    private String areaAtuacao;
    private Turno turno;
    private EstadoFuncionario estado;
    private String observacoes;
    private String senha;
    private String cpf; 

    public Funcionario() {
    }

    public Funcionario(String nome, Cargo cargo, String areaAtuacao, Turno turno, EstadoFuncionario estado, String senha) {
        this.nome = nome;
        this.cargo = cargo;
        this.areaAtuacao = areaAtuacao;
        this.turno = turno;
        this.estado = estado;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getAreaAtuacao() {
        return areaAtuacao;
    }

    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public EstadoFuncionario getEstado() {
        return estado;
    }

    public void setEstado(EstadoFuncionario estado) {
        this.estado = estado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getCpf(){
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
