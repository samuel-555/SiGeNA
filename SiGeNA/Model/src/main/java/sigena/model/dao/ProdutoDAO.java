
package sigena.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Produto;
import sigena.model.util.ConexaoDB;

public class ProdutoDAO {
    public void cadastrar(Produto produto/*, Fornecedor fornecedor*/) throws PersistenciaException{
        String sql = "INSERT INTO produtos(fornecedor_id, quantidade, nome, tipo, lote, validade, disponivel) values (?, ?, ?, ?, ?, ?, ?)";
        
        try{
            Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            //ps.setLong(1, fornecedor.getId());
            ps.setInt(2, produto.getQuantidade());
            ps.setString(3, produto.getNome());
            ps.setString(4, produto.getTipo());
            if(produto.getLote() != null){
            ps.setDate(5, java.sql.Date.valueOf(produto.getLote()));
            }else{
                ps.setNull(5, Types.DATE);
            }
            if(produto.getValidade() != null){
            ps.setDate(6, java.sql.Date.valueOf(produto.getValidade()));
            }else{
                ps.setNull(6, Types.DATE);
            }
            ps.setBoolean(7, produto.getDisponivel());
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
       
    }
    
    public List<Produto> listar() throws PersistenciaException{
        String sql = "SELECT * FROM produtos";
        List<Produto> produtos = new ArrayList<>();
        
        
        return produtos;
    }
}
