/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.domain;
import java.util.List;
import java.util.LinkedList;

public class AnimaisAlocados {
    
    private int tamanho;
    private List<Animal> animais;
    
    public AnimaisAlocados(){
        tamanho = 0;
        animais = new LinkedList<Animal>();
    }
    
    public void adicionar(Animal animal){
        animais.add(animal);
    }
    
    public void remover(Animal animal){
        animais.remove(animal);
    }
    
    public void listar(){
       for(int i = 0; i < animais.size(); i++)
           System.out.println(animais.get(i));
   }
    
    public int tamanho(){
        return tamanho;
    }
}
