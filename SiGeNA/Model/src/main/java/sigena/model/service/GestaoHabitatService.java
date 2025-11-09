package sigena.model.service;

import sigena.model.domain.Habitat;
import sigena.model.dao.HabitatDAO;
import sigena.model.domain.Animal;
import java.util.List;

public class GestaoHabitatService {
   
    private final HabitatDAO dao;
    
    public GestaoHabitatService(){
        dao = new HabitatDAO();
    }

    public void cadastrarHabitat(String tipo,String nome, int tamanho, boolean manutencao){
        Habitat habitat = new Habitat(tipo,nome,tamanho,manutencao);
        habitat.setCapacidade(tamanho);
        habitat.setDisponivel(true);
        dao.inserir(habitat);
    }
    
    public List<Habitat> listarHabitats() {
        return dao.listar();
    }
    
    public void editar(String nomeAntigo, String nome, String tipo, int tamanho, boolean manutencao) {
        Habitat habitat = new Habitat(tipo, nome, tamanho, manutencao);
        Habitat habitatAntigo = dao.buscar(nomeAntigo);
        
        habitat.setCapacidade(habitatAntigo.getCapacidade());
        habitat.setDisponivel(habitatAntigo.getDisponivel());
        dao.editar(nomeAntigo, habitat);
    }

    
    public void editarManutencao(String nomeHabitat, boolean manutencao){
        dao.editarManutencao(nomeHabitat, manutencao);
    }

    public void editarDisponivel(String nomeHabitat, boolean disponivel){
        dao.editarDisponivel(nomeHabitat, disponivel);
    }
    
    public void editarCapacidade(String nomeHabitat, int novaCapacidade){
        dao.editarCapacidade(nomeHabitat, novaCapacidade);
    }
    
    public Habitat buscar(String nome){
        return dao.buscar(nome);
    }
    public void excluir(Habitat habitat){
        dao.excluir(habitat);
    }
    
    public void inserirAnimalAlocado(String habitat, int animalId){
        dao.inserirAnimalAlocado(habitat,animalId);
    }
    
    public int calcularCapaciade(int capacidade,Animal animaisAlocados[]){
        int novaCapacidade;
        novaCapacidade = capacidade;
        
        for(int i = 0; i < animaisAlocados.length; i++){
            if(animaisAlocados[i].getPeso() <= 10)
                novaCapacidade-=1;
            else if(animaisAlocados[i].getPeso() <= 25)
                novaCapacidade-=3;
            else if(animaisAlocados[i].getPeso() > 25)
                novaCapacidade-=5;
            
            if(novaCapacidade < 0)
                novaCapacidade = 0;
        }
            
        return novaCapacidade;
    }
   
}
