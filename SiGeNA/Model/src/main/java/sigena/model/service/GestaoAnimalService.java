package sigena.model.service;

import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Animal;
import sigena.model.dao.AnimalDAO;

public class GestaoAnimalService {
    AnimalDAO dao = new AnimalDAO();
    
    public boolean cadastrarAnimal(Animal animal) throws PersistenciaException{
        if(animal.getEspecieNome() == null)
            return false;
        
        dao.cadastrar(animal);
        return true;
    }
    
    public List<Animal> listarAnimais() throws PersistenciaException{
        return dao.listar();
    }
    
    public void excluirAnimal(Long id) throws PersistenciaException{
        dao.excluir(id);
    }
    
    public Animal buscarAnimal(Long id) throws PersistenciaException{
        return dao.buscarPorId(id);
    }
    
    public void editarAnimal(Animal animal) throws PersistenciaException{
        dao.editar(animal);
    }
}
