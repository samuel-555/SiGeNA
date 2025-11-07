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
}
