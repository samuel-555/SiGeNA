/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.service;
import java.time.LocalDateTime;
import java.util.List;
import sigena.model.dao.TarefaDAO;
import sigena.model.domain.Tarefa;

public class GestaoTarefaService {
    
    private final TarefaDAO dao;
    
    public GestaoTarefaService(){
        dao = new TarefaDAO();
    }
    
    public void cadastrarTarefa(String nome, String texto, int id_destinatario, LocalDateTime dataPConclusao){
        Tarefa tarefa = new Tarefa(nome,texto,id_destinatario,dataPConclusao);//adicionar teste de validade da data    ! 
        dao.inserir(tarefa);
    }
    
    public List<Tarefa> listarTarefas() {
        return dao.listar();
    }
    
    public void editar(long id, String nome, String texto,int id_destinatario,LocalDateTime dataPConclusao) {
        Tarefa tarefa = new Tarefa(nome,texto,id_destinatario,dataPConclusao);
        
        tarefa.setNome(nome);
        tarefa.setTexto(texto);
        tarefa.setIdDestinatario(id_destinatario);
        tarefa.setDataPConclusao(dataPConclusao);
        tarefa.setDataCadastro(LocalDateTime.now());
        
        dao.editar(id,tarefa);
    }

    public void editarConcluida(long id, boolean concluida){
        dao.editarConcluida(id, concluida);
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
    
}
