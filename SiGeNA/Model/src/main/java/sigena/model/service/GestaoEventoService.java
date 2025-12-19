package sigena.model.service;

import java.time.LocalDateTime;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Evento;
import sigena.model.dao.EventoDAO;

public class GestaoEventoService {
    private final EventoDAO eventoDAO = new EventoDAO();
    
    public List<Evento> listarEventos() throws PersistenciaException{
        eventoDAO.atualizarOcorridos();
        
        return eventoDAO.listar();
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
    
    private boolean conferirCampos(Evento evento) {
        if(evento.getTitulo() == null || evento.getTitulo().replaceAll("\\s", "").equals(""))
            return false;
        
        if(evento.getDataProgramada() == null || !evento.getDataProgramada().isAfter(LocalDateTime.now()))
            return false;
        
        return true;
    }
}
