package sigena.model.domain;

public class ItemPlanoAlimentar {

    private Long id;
    private String alimento;
    private Double gramatura;
    private Integer vezesPorDia;

    public ItemPlanoAlimentar() {
    }

    public ItemPlanoAlimentar(String alimento, Double gramatura, Integer vezesPorDia) {
        this(null, alimento, gramatura, vezesPorDia);
    }

    public ItemPlanoAlimentar(Long id, String alimento, Double gramatura, Integer vezesPorDia) {
        this.id = id;
        this.alimento = alimento;
        this.gramatura = gramatura;
        this.vezesPorDia = vezesPorDia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlimento() {
        return alimento;
    }

    public void setAlimento(String alimento) {
        this.alimento = alimento;
    }

    public Double getGramatura() {
        return gramatura;
    }

    public void setGramatura(Double gramatura) {
        this.gramatura = gramatura;
    }

    public Integer getVezesPorDia() {
        return vezesPorDia;
    }

    public void setVezesPorDia(Integer vezesPorDia) {
        this.vezesPorDia = vezesPorDia;
    }
}

