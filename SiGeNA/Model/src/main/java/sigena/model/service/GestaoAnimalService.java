package sigena.model.service;

import java.time.LocalDate;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Animal;
import sigena.model.dao.AnimalDAO;
import sigena.model.dao.EspecieDAO;
import sigena.model.dao.HabitatDAO;
import sigena.model.domain.util.AnimalSexo;

public class GestaoAnimalService {
    private final AnimalDAO animalDAO = new AnimalDAO();
    private final EspecieDAO especieDAO = new EspecieDAO();
    private final HabitatDAO habitatDAO = new HabitatDAO();
    
    public boolean cadastrarAnimal(Animal animal) throws PersistenciaException{
        if(!conferirCampos(animal))
            return false;
        
        animalDAO.cadastrar(animal);
        habitatDAO.inserirAnimalAlocado(animal.getHabitatNome(), animal.getId());
        
        return true;
    }
    
    public List<Animal> listarAnimais(String busca, String filtro) throws PersistenciaException{
        return animalDAO.listar(busca, filtro);
    }
    
    public List<Animal> listarAnimais() throws PersistenciaException{
        return animalDAO.listar("", "");
    }
    
    public void excluirAnimal(Long id) throws PersistenciaException{
        animalDAO.excluir(id);
    }
    
    public Animal buscarAnimal(Long id) throws PersistenciaException{
        return animalDAO.buscarPorId(id);
    }
    
    public boolean editarAnimal(Animal animal) throws PersistenciaException{
        if(!conferirCampos(animal))
            return false;
        
        animalDAO.editar(animal);
        habitatDAO.editarAnimalAlocado(animal.getHabitatNome(), animal.getId());
        
        return true;
    }
    
    private boolean conferirCampos(Animal animal) {
        if(animal.getNome() == null || animal.getNome().replaceAll("\\s", "").equals(""))
            return false;
        
        try {
            if(animal.getEspecieNome() == null || especieDAO.buscarPorId(animal.getEspecieId()) == null)
                return false;
        } catch(PersistenciaException e) {
            return false;
        }

        try {
            AnimalSexo.setAnimalSexo(animal.getSexo());
        } catch(IllegalArgumentException e) {
            return false;
        }
        
        if(animal.getDataDeNascimentoOb() == null)
            return false;
        
        if(animal.getPeso() <= 0)
            return false;
        
        if(animal.getHabitatNome() == null || habitatDAO.buscar(animal.getHabitatNome()) == null)
            return false;
        
        if(!animal.getDataDeNascimentoOb().isAfter(LocalDate.now()))
            return false;
        
        return true;
    }
}
