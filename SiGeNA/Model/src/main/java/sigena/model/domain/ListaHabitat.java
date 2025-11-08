/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.domain;
import java.util.List;
import java.util.LinkedList;
/**
 *
 * @author USUARIO
 */
public class ListaHabitat {
    
   private Habitat habitat;
   private List<Habitat> habitats;
   
   public ListaHabitat(Habitat habitat){
       habitats = new LinkedList<Habitat>();
       this.habitat = habitat;
   }
   
   public void adicionar(){
       habitats.add(habitat);
   }
   
   public void remover(Habitat habitat){
       habitats.remove(habitat);
   }
   
   public void listar(){
       for(int i = 0; i < habitats.size(); i++)
           System.out.println(habitats.get(i));
   }
}
