package sigena.model.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import sigena.model.domain.Turno;

public class Visita {

    private Long id;
    private String nomeVisitante;
    private String documento;
    private String motivo;
    private LocalDate dataVisita;
    private String observacoes;
    private LocalDateTime dataRegistro;
    private boolean vip;
    private boolean necessidadeEspecial;
    private String descricaoNecessidade;
    private Turno turno;

    public Visita() {
    }

    public Visita(String nomeVisitante, String documento, String motivo, LocalDate dataVisita, String observacoes) {
        this.nomeVisitante = nomeVisitante;
        this.documento = documento;
        this.motivo = motivo;
        this.dataVisita = dataVisita;
        this.observacoes = observacoes;
    }

    public Visita(Long id, String nomeVisitante, String documento, String motivo, LocalDate dataVisita, String observacoes) {
        this(nomeVisitante, documento, motivo, dataVisita, observacoes);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeVisitante() {
        return nomeVisitante;
    }

    public void setNomeVisitante(String nomeVisitante) {
        this.nomeVisitante = nomeVisitante;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(LocalDate dataVisita) {
        this.dataVisita = dataVisita;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public boolean isNecessidadeEspecial() {
        return necessidadeEspecial;
    }

    public void setNecessidadeEspecial(boolean necessidadeEspecial) {
        this.necessidadeEspecial = necessidadeEspecial;
    }

    public String getDescricaoNecessidade() {
        return descricaoNecessidade;
    }

    public void setDescricaoNecessidade(String descricaoNecessidade) {
        this.descricaoNecessidade = descricaoNecessidade;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
}
