package sigena.model.service;

import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Evento;
import sigena.model.dao.EventoDAO;

public class GestaoEventoService {
    private final EventoDAO eventoDAO = new EventoDAO();
    
    public List<Evento> listarEventos() throws PersistenciaException{
        return eventoDAO.listar();
    }
    
    public boolean cadastrarEvento(Evento evento) throws PersistenciaException {
        eventoDAO.cadastrar(evento);
        return true;
    }
}
