package sigena.model.service;

import sigena.model.domain.Habitat;
import sigena.model.dao.HabitatDAO;
import sigena.model.dao.AnimalDAO;
import sigena.model.domain.Animal;
import java.util.List;

public class GestaoHabitatService {
   
    private final HabitatDAO dao;
    private final AnimalDAO animalDao;
    
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
        
        if(habitatAntigo.getTamanho() !=tamanho){
            if (tamanho > habitatAntigo.getTamanho())
                habitat.setCapacidade(habitatAntigo.getCapacidade()+ tamanho);
            else
                habitat.setCapacidade(habitatAntigo.getCapacidade() - tamanho);
        }
        else
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
    
    public void editarCapacidade(String nomeHabitat,int animalId){
        Habitat habitat = dao.buscar(nomeHabitat);
        Animal animal = animalDao.buscarPorId(animalId);
        
        int novaCapacidade = calcularCapacidade(habitat.getCapacidade(),animal);
        
        if(novaCapacidade < 1){
            habitat.setDisponivel(false);
            dao.editarDisponivel(nomeHabitat, false);
        }
            
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
        editarCapacidade(habitat,animalId);
    }
    
    public int calcularCapacidade(int capacidade,Animal animal){
        int novaCapacidade;
        novaCapacidade = capacidade;
        
        if(animal.getPeso() <= 10)
            novaCapacidade-=1;
        else if(animal.getPeso() <= 25)
            novaCapacidade-=3;
        else if(animal.getPeso() > 25)
            novaCapacidade-=5;
            
        if(novaCapacidade < 0)
           novaCapacidade = 0;
       
        return novaCapacidade;
    }
   
}
