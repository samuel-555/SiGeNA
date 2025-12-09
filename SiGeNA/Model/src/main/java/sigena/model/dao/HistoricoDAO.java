/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import sigena.model.domain.Historico;
import sigena.model.domain.TipoHistorico;
import sigena.model.util.ConexaoDB;

public class HistoricoDAO {
    
    public void inserir(Historico historico){
        
        String sql = "INSERT INTO historico(funcionario_id, tipo, descricao, data) VALUES(?,?,?,?)";
        
        try (Connection con = ConexaoDB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1,historico.getIdFuncionario());
            ps.setString(2,historico.getTipo().name());
            ps.setString(3,historico.getDescricao());
            ps.setTimestamp(4, Timestamp.valueOf(historico.getData()));

            ps.executeUpdate();
        } 
        catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
    
    public List<Historico> listarPorFuncionario(int id_funcionario) {

        List<Historico> lista = new ArrayList<>();

        String sql = "SELECT tipo, descricao, data FROM historico WHERE id_funcionario = ?";

        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            ps.setInt(1,id_funcionario);
            
            while(rs.next()){
                Historico historico = new Historico(
                    TipoHistorico.valueOf(rs.getString("tipo")),
                    rs.getString("descricao"),
                    rs.getObject("data")
            );
            lista.add(historico);
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return lista;
    }


//fazer isso aqui com filtro de pesquisa por tipo bem ninja    
    public Historico buscar(int id_funcionario) { 
        String sql = "SELECT tipo, descricao, data FROM historico WHERE funcionario_id = ?";


        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1,id_funcionario);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Historico historico = new Historico(
                        rs.getString("tipo"),
                        rs.getString("descricao"),
                        rs.getObjetc("data")
                );
               
                return historico;
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }   

        return null;
    }

}
