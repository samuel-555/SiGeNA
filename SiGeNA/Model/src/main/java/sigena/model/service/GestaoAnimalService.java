package sigena.model.service;

import java.util.List;
import sigena.model.domain.Animal;
import sigena.model.dao.AnimalDAO;

public class GestaoAnimalService {
    AnimalDAO dao = new AnimalDAO();
    
    public void cadastrarAnimal(Animal animal) {
        dao.cadastrar(animal);
    }
    
    public List<Animal> listarAnimais() {
        return dao.listar();
    }
    
    public void excluirAnimal(String id) {
        dao.excluir(id);
    }
    
    public Animal buscarAnimal(String id) {
        return dao.buscarPorId(id);
    }
    
    public void editarAnimal(Animal animal) {
        dao.editar(animal);
    }
}
