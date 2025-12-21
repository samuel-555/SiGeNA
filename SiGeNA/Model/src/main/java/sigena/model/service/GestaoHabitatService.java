package sigena.model.service;

import sigena.model.domain.Habitat;
import sigena.model.dao.HabitatDAO;
import sigena.model.dao.AnimalDAO;
import sigena.model.domain.Animal;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.exception.HabitatVazioException;
import sigena.model.domain.TipoHistorico;

public class GestaoHabitatService {
   
    private final HabitatDAO dao;
    private final AnimalDAO animalDao;
    private final GestaoHistoricoService historicoService;
    
    public GestaoHabitatService(){
        dao = new HabitatDAO();
        animalDao = new AnimalDAO();
        historicoService = new GestaoHistoricoService();
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
        
        if(habitatAntigo.getTamanho() != tamanho){
            int novaCapacidade = tamanho;
            int ocupadoAntigo = (habitatAntigo.getTamanho() - habitatAntigo.getCapacidade());
            
            if(ocupadoAntigo > novaCapacidade){
                    habitat.setCapacidade(0);
                    habitat.setDisponivel(false);
                }
            else{
                habitat.setCapacidade(novaCapacidade - ocupadoAntigo);
            }
        }
        else
            habitat.setCapacidade(habitatAntigo.getCapacidade());
        
        
        habitat.setDisponivel(habitatAntigo.getDisponivel());
        dao.editar(nomeAntigo, habitat);
    }

    
    public void editarManutencao(String nomeHabitat, boolean manutencao, String cpfLogado) {

        Habitat habitatAtual = dao.buscar(nomeHabitat);

        if (habitatAtual.getManutencao() == manutencao) {
            return; 
        }

        dao.editarManutencao(nomeHabitat, manutencao);

        if (manutencao) {
            historicoService.registrar(TipoHistorico.MANUTENCAO,TipoHistorico.MANUTENCAO.getDescricao(nomeHabitat),cpfLogado);
        }
    }

    public void editarDisponivel(String nomeHabitat, boolean disponivel){
        dao.editarDisponivel(nomeHabitat, disponivel);
    }
    
    public void editarCapacidade(String nomeHabitat, long animalId) throws PersistenciaException{
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
    
    public List<Habitat> buscarPorNomeOuTipo(String termo) {

        if (termo == null || termo.isBlank()) 
            return dao.listar();
    
        return dao.buscarPorNomeOuTipo(termo);
    }
    
    public void excluir(Habitat habitat) throws HabitatVazioException {
        if(!habitat.getVazio())
            throw new HabitatVazioException("Não é permitido deletar um habitat com animais alocados");
        
        dao.excluir(habitat);
    }
    
    public void inserirAnimalAlocado(String habitat, long animalId) throws PersistenciaException{
        dao.inserirAnimalAlocado(habitat,animalId);
        editarCapacidade(habitat,animalId);
        
        Animal animal = animalDao.buscarPorId(animalId);
        if(animal.getHostilidade() == true)
            editarDisponivel(habitat, false);
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
