/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import sigena.model.dao.HistoricoDAO;
import sigena.model.domain.Historico;
import sigena.model.domain.TipoHistorico;

public class GestaoHistoricoService {
    private final HistoricoDAO dao;
    
    public GestaoHistoricoService(){
        dao = new HistoricoDAO();
    }
    
    public void registrar(TipoHistorico tipo, String descricao, String funcionarioCpf) {
        Historico historico = new Historico(funcionarioCpf, descricao,new Date(),tipo);
        dao.inserir(historico);
    }
    
    public List<Historico> listarPorFuncionario(String cpf) {
        return dao.listarPorFuncionario(cpf);
    }
    
    public List<Historico> buscarPorTipo(TipoHistorico tipo){
        return dao.buscarPorTipo(tipo);
    }
    
    public List<Historico> buscarPorFuncionario(String nome){
        return dao.buscarPorFuncionario(nome);
    }
    
    public Map<String, List<Historico>> listarAgrupadoPorFuncionario() {

        List<Historico> lista = dao.listarTodos();
        Map<String, List<Historico>> map = new LinkedHashMap<>();

        for (Historico h : lista) {
            map.computeIfAbsent(
                h.getFuncionarioCpf(),
                k -> new ArrayList<>()
            ).add(h);
        }

        return map;
    }
    
    public Map<String, List<Historico>> buscarAgrupado(String termo) {
        List<Historico> lista;
        
        if (termo == null || termo.isBlank())
            return listarAgrupadoPorFuncionario();
        
        TipoHistorico tipo = TipoHistorico.from(termo);
        if (tipo != null)
            lista = dao.buscarPorTipo(tipo);
        else
            lista = dao.buscarPorFuncionario(termo);

        return lista.stream()
            .collect(Collectors.groupingBy(Historico::getFuncionarioCpf));
    }
}   
