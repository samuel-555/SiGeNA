package sigena.model.domain;

import java.time.LocalDate;
import sigena.model.domain.util.TipoProduto;

public class Produto {

    private String nome;
    private Long id;
    //private Fornecedor fornecedor;
    private int quantidade;
    private LocalDate validade;
    private LocalDate lote;
    private Boolean disponivel;
    private TipoProduto tipo;

    public Produto(String nome,/* Fornecedor fornecedor,*/ int quantidade, LocalDate validade, LocalDate lote, Boolean disponivel, TipoProduto tipo) {
        this.nome = nome;
        //this.fornecedor = fornecedor;
        this.quantidade = quantidade;
        this.validade = validade;
        this.lote = lote;
        this.disponivel = disponivel;
        this.tipo = tipo;
    }
    
    public Produto(String nome,/* Fornecedor fornecedor,*/ int quantidade, LocalDate validade, LocalDate lote, TipoProduto tipo) {
        this.nome = nome;
        //this.fornecedor = fornecedor;
        this.quantidade = quantidade;
        this.validade = validade;
        this.lote = lote;
        this.tipo = tipo;
    }
    
    public Produto() {
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
    
    /*public void setFornecedor(fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }*/

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
    
    public void setTipo(TipoProduto tipo) {
        this.tipo = tipo;
    }

    public TipoProduto getTipo() {
        return tipo;
    }
}
