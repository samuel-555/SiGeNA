package sigena.model.domain;

public class Animal {
    private String nome;
    private String sexo;
    private String especie;
    private Integer idade;
    private Double peso;
    private String habitat;
    private boolean hostil;
    
    public Animal(String nome, String especie, Integer idade, Double peso, String habitat) {
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.peso = peso;
        this.habitat = habitat;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getEspecie() {
        return especie;
    }
    
    public Integer getIdade() {
        return idade;
    }
    
    public Double getPeso() {
        return peso;
    }
    
    public String getHabitat() {
        return habitat;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setEspecie(String especie) {
        this.especie = especie;
    }
    
    public void setIdade(Integer idade) {
        this.idade = idade;
    }
    
    public void setPeso(Double peso) {
        this.peso = peso;
    }
    
    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }
}
