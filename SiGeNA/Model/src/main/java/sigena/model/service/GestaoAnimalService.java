package sigena.model.service;

import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Animal;
import sigena.model.dao.AnimalDAO;
import sigena.model.dao.HabitatDAO;

public class GestaoAnimalService {
    AnimalDAO animalDAO = new AnimalDAO();
    HabitatDAO habitatDAO = new HabitatDAO();
    
    public boolean cadastrarAnimal(Animal animal) throws PersistenciaException{
        if(animal.getEspecieNome() == null)
            return false;
        
        animalDAO.cadastrar(animal);
        System.out.println(animal.getHabitatNome());
        habitatDAO.inserirAnimalAlocado(animal.getHabitatNome(), animal.getId());
        
        return true;
    }
    
    public List<Animal> listarAnimais() throws PersistenciaException{
        return animalDAO.listar();
    }
    
    public void excluirAnimal(Long id) throws PersistenciaException{
        animalDAO.excluir(id);
    }
    
    public Animal buscarAnimal(Long id) throws PersistenciaException{
        return animalDAO.buscarPorId(id);
    }
    
    public void editarAnimal(Animal animal) throws PersistenciaException{
        animalDAO.editar(animal);
        habitatDAO.editarAnimalAlocado(animal.getHabitatNome(), animal.getId());
    }
}
