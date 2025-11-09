package sigena.model.domain;

public class Especie {
    private int id;
    private boolean predador;
    private String nome;
    private String classe;
    private String observacoes;
    private String habitat;
    private String alimentacao;
    
    public int getId() {
        return id;
    }

    public boolean isPredador() {
        return predador;
    }

    public String getNome() {
        return nome;
    }

    public String getClasse() {
        return classe;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public String getHabitat() {
        return habitat;
    }

    public String getAlimentacao() {
        return alimentacao;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setPredador(boolean predador) {
        this.predador = predador;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public void setAlimentacao(String alimentacao) {
        this.alimentacao = alimentacao;
    }
    
    
}
