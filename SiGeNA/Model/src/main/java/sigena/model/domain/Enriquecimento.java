package sigena.model.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Enriquecimento {
    private Integer id;
    private String nome;
    private String tipo;
    private String especieDestinada;
    private String frequencia;
    private String observacoes;
    private LocalDateTime dataCriacao;
    private List<String> habitats; 

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEspecieDestinada() { return especieDestinada; }
    public void setEspecieDestinada(String especieDestinada) { this.especieDestinada = especieDestinada; }
    public String getFrequencia() { return frequencia; }
    public void setFrequencia(String frequencia) { this.frequencia = frequencia; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public List<String> getHabitats() { return habitats; }
    public void setHabitats(List<String> habitats) { this.habitats = habitats; }
}