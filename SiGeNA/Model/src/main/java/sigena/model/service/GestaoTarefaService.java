/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.service;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import sigena.model.common.exception.DataInvalidaException;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.TarefaDAO;
import sigena.model.domain.Tarefa;
import sigena.model.domain.TipoHistorico;


public class GestaoTarefaService {
    
    private final TarefaDAO dao;
    private final GestaoHistoricoService historicoService;
            
    public GestaoTarefaService(){
        dao = new TarefaDAO();
        historicoService = new GestaoHistoricoService();
    }
    
    public void cadastrarTarefa(String nome, String texto, int id_destinatario, LocalDateTime dataPConclusao) throws DataInvalidaException{
        if(!validarData(dataPConclusao))
            throw new DataInvalidaException("Data inválida!");
        
        Tarefa tarefa = new Tarefa(nome,texto,id_destinatario,dataPConclusao); 
        dao.inserir(tarefa);
    }
    
    public List<Tarefa> listarTarefas() {
        return dao.listar();
    }
    
    public List<Tarefa> listarPorUsuario(int id) throws PersistenciaException, SQLException{
        return dao.listarPorUsuario(id);
    }
    
    public void editar(long id, String nome, String texto,int id_destinatario,LocalDateTime dataPConclusao) throws DataInvalidaException {
        if(!validarData(dataPConclusao))
            throw new DataInvalidaException("Data inválida!");
        
        Tarefa tarefa = new Tarefa(nome,texto,id_destinatario,dataPConclusao);
        
        tarefa.setId(id);
        tarefa.setNome(nome);
        tarefa.setTexto(texto);
        tarefa.setIdDestinatario(id_destinatario);
        tarefa.setDataPConclusao(dataPConclusao);
        tarefa.setDataCadastro(LocalDateTime.now());
        
        dao.editar(id,tarefa);
    }

    public void editarConcluida(long id, boolean concluida, String cpf){
        dao.editarConcluida(id, concluida);
        historicoService.registrar(TipoHistorico.TAREFA,TipoHistorico.TAREFA.getDescricao(dao.buscar(id)),cpf);
    }
    
    public Tarefa buscar(long id){
        return dao.buscar(id);
    }
    
    public void excluir(Tarefa tarefa){
        dao.excluir(tarefa);
    }
    
    public boolean validarData(LocalDateTime data){
        LocalDateTime agora = LocalDateTime.now();
        if(data.isAfter(agora))
            return true;
        return false;
    }
    
    public List<Tarefa> listarTarefasDoDia(){
        return dao.listarDoDia();
    }
    
    public List<Tarefa> listarTarefasDoDiaPorUsuario(int id){
        return dao. listarDoDiaPorUsuario(id);
    }
}
