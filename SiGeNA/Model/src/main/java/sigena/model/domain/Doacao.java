package sigena.model.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Doacao {

    private Long id;
    private String nomeDoador;
    private DoacaoTipo tipo;
    private Double valorMonetario;
    private String descricaoOutro;
    private LocalDate dataDoacao;
    private String observacoes;
    private StatusDoacao status;
    private boolean reciboEmitido;
    private LocalDateTime dataRegistro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDoador() {
        return nomeDoador;
    }

    public void setNomeDoador(String nomeDoador) {
        this.nomeDoador = nomeDoador;
    }

    public DoacaoTipo getTipo() {
        return tipo;
    }

    public void setTipo(DoacaoTipo tipo) {
        this.tipo = tipo;
    }

    public Double getValorMonetario() {
        return valorMonetario;
    }

    public void setValorMonetario(Double valorMonetario) {
        this.valorMonetario = valorMonetario;
    }

    public String getDescricaoOutro() {
        return descricaoOutro;
    }

    public void setDescricaoOutro(String descricaoOutro) {
        this.descricaoOutro = descricaoOutro;
    }

    public LocalDate getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(LocalDate dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public StatusDoacao getStatus() {
        return status;
    }

    public void setStatus(StatusDoacao status) {
        this.status = status;
    }

    public boolean isReciboEmitido() {
        return reciboEmitido;
    }

    public void setReciboEmitido(boolean reciboEmitido) {
        this.reciboEmitido = reciboEmitido;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public boolean isMonetaria() {
        return this.tipo == DoacaoTipo.MONETARIA;
    }

    public boolean isOutro() {
        return this.tipo == DoacaoTipo.OUTRO;
    }
}
