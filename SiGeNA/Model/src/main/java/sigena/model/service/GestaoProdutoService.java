package sigena.model.service;

import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.ProdutoDAO;
import sigena.model.domain.Produto;

public class GestaoProdutoService {
    ProdutoDAO dao = new ProdutoDAO();
    
    public void cadastrar(Produto produto) throws PersistenciaException{ 
        if(produto.getQuantidade() > 0){
            produto.setDisponivel(true);
        }
        dao.cadastrar(produto);
        
    }
}
