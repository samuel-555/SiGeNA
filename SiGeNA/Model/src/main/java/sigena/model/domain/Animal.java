package sigena.model.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import sigena.model.domain.util.AnimalSexo;
import sigena.model.domain.util.DataConverter;

public class Animal {
    private Long id;
    private String nome;
    private Especie especie;
    private AnimalSexo sexo;
    private LocalDate dataDeNascimento;
    private Double peso;
    private boolean hostil;
    
    public Animal(String nome, Especie especie, String sexo, String dataDeNascimento, Double peso, boolean hostil) {
        this.nome = nome;
        this.especie = especie;
        this.sexo = AnimalSexo.setAnimalSexo(sexo);
        this.dataDeNascimento = DataConverter.toLocalDate(dataDeNascimento);
        this.peso = peso;
        this.hostil = hostil;
    }
    
    public Animal(Long id, String nome, Especie especie, String sexo, String dataDeNascimento, Double peso, boolean hostil) {
        this.nome = nome;
        this.especie = especie;
        this.sexo = AnimalSexo.setAnimalSexo(sexo);
        this.dataDeNascimento = DataConverter.toLocalDate(dataDeNascimento);
        this.peso = peso;
        this.hostil = hostil;
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getEspecieNome() {
        if(especie != null)
            return especie.getNome();
        
        return null;
    }
    
    public Integer getEspecieId() {
        if(especie != null)
            return especie.getId();
        
        return null;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getSexo() {
        return sexo.getSexo();
    }
    
    public void setSexo(String sexo) {
        this.sexo = AnimalSexo.setAnimalSexo(sexo);
    }
    
    public String getDataDeNascimentoFormat() {
        return DataConverter.toStringFormat(dataDeNascimento);
    }
    
    public LocalDate getDataDeNascimentoOb() {
        return dataDeNascimento;
    }
    
    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = DataConverter.toLocalDate(dataDeNascimento);
    }
    
    public String getIdade() {
        return DataConverter.toAge(this.dataDeNascimento);
    }
    
    public Double getPeso() {
        return this.peso;
    }
    
    public void setPeso(Double peso) {
        this.peso = peso;
    }
    
    public boolean getHostilidade() {
        return hostil;
    }
    
    public void setHostilidade(boolean hostil) {
        this.hostil = hostil;
    }
}
