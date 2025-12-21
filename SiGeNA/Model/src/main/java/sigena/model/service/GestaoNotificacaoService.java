package sigena.model.service;

import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.NotificacaoDAO;
import sigena.model.domain.Notificacao;

public class GestaoNotificacaoService {
    
    NotificacaoDAO dao = new NotificacaoDAO();
    
    public void salvar(Notificacao n) throws PersistenciaException{
        dao.salvar(n);
    }
    
    public void criarParaTodos(String mensagem) throws PersistenciaException {
        dao.criarParaTodos(mensagem);
    }
    
    public List<Notificacao> listarPorUsuario(int idDestinatario){
        return dao.listarPorUsuario(idDestinatario);
    }

    public void marcarComoLida(Notificacao n) {
        dao.marcarComoLida(n);
    }
    
    public Notificacao buscarPorId(Long id){
        return dao.buscarPorId(id);
    }
}
