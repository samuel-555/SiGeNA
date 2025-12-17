package sigena.model.service;

import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.ProdutoDAO;
import sigena.model.domain.Fornecedor;
import sigena.model.domain.Produto;

public class GestaoProdutoService {
    private final ProdutoDAO dao = new ProdutoDAO();
    
    public void cadastrar(Produto p, Fornecedor f) throws PersistenciaException{ 
        dao.cadastrar(p, f);
    }
    
     public List<Produto> listar() throws PersistenciaException {
        return dao.listar();
    }

    public Produto buscar(Long id) throws PersistenciaException {
        return dao.buscar(id);
    }

    public void excluir(Long id) throws PersistenciaException {
        dao.excluir(id);
    }

    public void alterar(Produto p) throws PersistenciaException {
        dao.alterar(p);
    }
}
