/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.domain;
import sigena.model.service.GestaoHabitatService;


public class Habitat {
    private String tipo;
    private String nome;
    private int capacidade;
    private int tamanho;
    private boolean manutencao;
    private boolean disponivel;
    private AnimaisAlocados animaisAlocados;
            
    public Habitat(String tipo,String nome, int tamanho, boolean manutencao){
        this.tipo = tipo;
        this.nome = nome;
        this.tamanho = tamanho;
        this.manutencao = manutencao;
        disponivel = true;
        animaisAlocados = new AnimaisAlocados();
        capacidade = tamanho;
    }
    
    public void setAnimalAlocado(Animal animal){
        animaisAlocados.adicionar(animal);
    }
    public void setDisponivel(boolean disponivel){
        this.disponivel = disponivel;
    }
    public void setManutencao(boolean manutencao){
        this.manutencao = manutencao;
    }
    public void setTamanho(int tamanho){
        this.tamanho = tamanho;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    public void setCapacidade(int capacidade){
        this.capacidade = capacidade;
    }
    
    
    public void getAnimalAlocado(){
        animaisAlocados.listar();
    }
    public String getNome(){
        return nome;
    }
    public String getTipo(){
        return tipo;
    }
    public int getTamanho(){
        return tamanho;
    }
    public boolean getManutencao(){
        return manutencao;
    }
    public boolean getDisponivel(){
        return disponivel;
    }
    public int getCapacidade(){
        return capacidade;
    }
    
}
