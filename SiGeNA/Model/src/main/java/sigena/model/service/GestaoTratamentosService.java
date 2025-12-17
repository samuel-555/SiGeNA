package sigena.model.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import sigena.model.common.exception.DatabaseException;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.TratamentoDAO;
import sigena.model.domain.Animal;
import sigena.model.domain.Tratamento;
import sigena.model.domain.Usuario;
import sigena.model.domain.util.StatusTratamento;

public class GestaoTratamentosService {

    TratamentoDAO dao = new TratamentoDAO();

    public void cadastrar(Animal animal, Usuario usuario, Tratamento tratamento) throws PersistenciaException {
        LocalDate hoje = LocalDate.now();
        LocalDate dataFinal = tratamento.getDataFinal();
        if (dataFinal.isEqual(hoje) || dataFinal.isBefore(hoje)) {
            tratamento.setStatusTratamento(StatusTratamento.CONCLUIDO);
        } else {
            tratamento.setStatusTratamento(StatusTratamento.EM_ANDAMENTO);
        }
        dao.cadastrar(animal, usuario, tratamento);
    }

    public void editar(Tratamento tratamento) throws PersistenciaException {
        LocalDate hoje = LocalDate.now();
        LocalDate dataFinal = tratamento.getDataFinal();
        if (dataFinal.isEqual(hoje) || dataFinal.isBefore(hoje)) {
            tratamento.setStatusTratamento(StatusTratamento.CONCLUIDO);
        } else {
            tratamento.setStatusTratamento(StatusTratamento.EM_ANDAMENTO);
        }
        dao.editar(tratamento);
    }
    
    public List<Tratamento> listar() throws PersistenciaException, DatabaseException{
       return dao.listar();
    }
    
    public Tratamento buscarPorId(int id) throws PersistenciaException, DatabaseException{
        return dao.buscarPorId(id);
    }
    
    public void cancelar(int id) throws PersistenciaException, DatabaseException{
        dao.cancelar(id);
    }
}
