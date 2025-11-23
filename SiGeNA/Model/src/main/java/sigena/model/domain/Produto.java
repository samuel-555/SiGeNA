package sigena.model.domain;

import java.time.LocalDate;

public class Produto {

    private String nome;
    private Long id;
    //private Fornecedor fornecedor;
    private int quantidade;
    private LocalDate validade;
    private LocalDate lote;
    private Boolean disponivel;

    public Produto(String nome, int quantidade, LocalDate validade, LocalDate lote, Boolean disponivel) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.validade = validade;
        this.lote = lote;
        this.disponivel = disponivel;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setLote(LocalDate lote) {
        this.lote = lote;
    }

    public LocalDate getLote() {
        return lote;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }
}
