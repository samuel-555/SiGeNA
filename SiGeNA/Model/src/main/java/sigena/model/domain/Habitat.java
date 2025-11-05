/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.domain;

/**
 *
 * @author USUARIO
 */
public class Habitat {
    private String tipo;
    private String nome;
    private int tamanho;//---------scapacidade----------
    private boolean precisaDeManutencao;
    private boolean disponivel;
        
    public Habitat(String tipo,String nome, int tamanho, boolean manutencao){
        this.tipo = tipo;
        this.nome = nome;
        this.tamanho = tamanho;
        this.precisaDeManutencao = manutencao;
        disponivel = true;
    }
    
    public void setDisponivel(boolean disponivel){
        this.disponivel = disponivel;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public String getTipo(){
        return tipo;
    }
    public int getTamanho(){
        return tamanho;
    }
    public boolean precisaDeManutencao(){
        return precisaDeManutencao;
    }
    
}
