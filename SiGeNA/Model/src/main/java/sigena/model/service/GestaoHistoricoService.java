/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.service;
import java.time.LocalDateTime;
import java.util.List;
import sigena.model.dao.HistoricoDAO;
import sigena.model.domain.Historico;
import sigena.model.domain.TipoHistorico;

public class GestaoHistoricoService {
    private final HistoricoDAO dao;
    
    public GestaoHistoricoService(){
        dao = new HistoricoDAO();
    }
    
    public void registrar(TipoHistorico tipo, String descricao, int idFuncionario) {
        Historico historico = new Historico(idFuncionario, descricao, LocalDateTime.now(),tipo);
        dao.inserir(historico);
    }
    
    public List<Historico> listarPorFuncionario(int id) {
        return dao.listarPorFuncionario(id);
    }
    
    public List<Historico> buscarPorTipo(TipoHistorico tipo){
        return dao.buscarPorTipo(tipo);
    }
    
    public List<Historico> buscarPorFuncionario(String nome){
        return dao.buscarPorFuncionario(nome);
    }
}
