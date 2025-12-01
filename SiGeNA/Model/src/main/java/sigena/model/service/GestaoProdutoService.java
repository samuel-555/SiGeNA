package sigena.model.service;

import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.ProdutoDAO;
import sigena.model.domain.Produto;

public class GestaoProdutoService {
    private final ProdutoDAO dao = new ProdutoDAO();
    
    public void cadastrar(Produto produto) throws PersistenciaException{ 
        dao.cadastrar(produto);
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
        ProdutoDAO dao = new ProdutoDAO();
        dao.alterar(p);
    }
}
