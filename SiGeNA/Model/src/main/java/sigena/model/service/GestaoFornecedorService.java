package sigena.model.service;

import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.FornecedorDAO;
import sigena.model.domain.Fornecedor;

public class GestaoFornecedorService {
    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    
    public boolean cadastrarFornecedor(Fornecedor fornecedor) throws PersistenciaException{
        
        
        return true;
    }
}
