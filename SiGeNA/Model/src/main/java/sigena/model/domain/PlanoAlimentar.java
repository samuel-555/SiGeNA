package sigena.model.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlanoAlimentar {

    private Long id;
    private Animal animal;
    private LocalDateTime dataCriacao;
    private final List<ItemPlanoAlimentar> itens = new ArrayList<>();

    public PlanoAlimentar() {
    }

    public PlanoAlimentar(Animal animal) {
        this.animal = animal;
    }

    public PlanoAlimentar(Long id, Animal animal) {
        this.id = id;
        this.animal = animal;
    }

    public PlanoAlimentar(Long id, Animal animal, LocalDateTime dataCriacao) {
        this(id, animal);
        this.dataCriacao = dataCriacao;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<ItemPlanoAlimentar> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void setItens(List<ItemPlanoAlimentar> itens) {
        this.itens.clear();
        if (itens != null) {
            this.itens.addAll(itens);
        }
    }

    public void addItem(ItemPlanoAlimentar item) {
        if (item != null) {
            this.itens.add(item);
        }
    }
}