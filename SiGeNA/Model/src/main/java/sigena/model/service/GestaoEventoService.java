package sigena.model.service;

import java.time.LocalDateTime;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Evento;
import sigena.model.dao.EventoDAO;
import java.time.LocalDateTime;

public class GestaoEventoService {
    private final EventoDAO eventoDAO = new EventoDAO();
    
    public List<Evento> listarEventos(String busca, String filtro, String tipo, LocalDateTime inicio, LocalDateTime fim) throws PersistenciaException{
        if(inicio != null && fim != null && fim.isBefore(inicio))
            throw new IllegalArgumentException("A data de início deve ser anterior ao fim");
        
        eventoDAO.atualizarOcorridos();
        
        return eventoDAO.listar(busca, filtro, tipo, inicio, fim);
    }
    
    public List<Evento> listarEventos(LocalDateTime inicio, LocalDateTime fim) throws PersistenciaException{
        return listarEventos("", "", "", inicio, fim);
    }
    
    public boolean cadastrarEvento(Evento evento) throws PersistenciaException {
        if(!conferirCampos(evento))
            return false;
        
        eventoDAO.cadastrar(evento);
        return true;
    }
    
    public void excluirEvento(Long id) throws PersistenciaException {
        eventoDAO.excluir(id);
    }
    
    public boolean editarEvento(Evento evento) throws PersistenciaException {
        if(!conferirCampos(evento))
            return false;
        
        eventoDAO.editar(evento);
        
        return true;
    }
    
    public Evento buscarEvento(Long id) throws PersistenciaException {
        return eventoDAO.buscarPorId(id);
    }
    
    public void cancelarEvento(Long id) throws PersistenciaException {
        eventoDAO.cancelar(id);
    }
    
    public void ativarEvento(Long id) throws PersistenciaException {
        eventoDAO.ativar(id);
    }
    
    private boolean conferirCampos(Evento evento) {
        if(evento.getTitulo() == null || evento.getTitulo().replaceAll("\\s", "").equals(""))
            return false;
        
        if(evento.getDataProgramada() == null || !evento.getDataProgramada().isAfter(LocalDateTime.now()))
            return false;
        
        return true;
    }
}
