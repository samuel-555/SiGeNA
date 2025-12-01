package sigena.model.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RelatorioSaude {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Long id;
    private Animal animal;
    private LocalDate dataRelatorio;
    private Double peso;
    private String status;
    private String observacoes;

    public RelatorioSaude() {
    }

    public RelatorioSaude(Animal animal, LocalDate dataRelatorio, Double peso, String status, String observacoes) {
        this.animal = animal;
        this.dataRelatorio = dataRelatorio;
        this.peso = peso;
        this.status = status;
        this.observacoes = observacoes;
    }

    public RelatorioSaude(Long id, Animal animal, LocalDate dataRelatorio, Double peso, String status, String observacoes) {
        this(animal, dataRelatorio, peso, status, observacoes);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public LocalDate getDataRelatorio() {
        return dataRelatorio;
    }

    public void setDataRelatorio(LocalDate dataRelatorio) {
        this.dataRelatorio = dataRelatorio;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getDataRelatorioFormatado() {
        if (dataRelatorio == null) {
            return "";
        }
        return dataRelatorio.format(DATE_FORMAT);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}

