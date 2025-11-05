package sigena.model.domain;

import java.time.LocalDate;

public class Funcionario {
    private static int contador = 1;

    private int id;
    private String nome;
    private Cargo cargo;
    private String areaAtuacao;
    private String observacoes;
    private EstadoFuncionario estado;
    private LocalDate dataCadastro;

    public Funcionario() {
        this.id = contador++;
        this.dataCadastro = LocalDate.now();
        this.estado = EstadoFuncionario.ATIVO;
    }

    // Getters e Setters
    public int getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }

    public String getAreaAtuacao() { return areaAtuacao; }
    public void setAreaAtuacao(String areaAtuacao) { this.areaAtuacao = areaAtuacao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public EstadoFuncionario getEstado() { return estado; }
    public void setEstado(EstadoFuncionario estado) { this.estado = estado; }

    public LocalDate getDataCadastro() { return dataCadastro; }
}
