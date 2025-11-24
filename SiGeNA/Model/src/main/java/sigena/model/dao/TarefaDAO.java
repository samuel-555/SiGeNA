/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.dao;

import java.sql.Connection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sigena.model.domain.Tarefa;
import sigena.model.util.ConexaoDB;

public class TarefaDAO {
    
     public void inserir(Tarefa tarefa){
        
        String sql = "INSERT INTO tarefas(nome,texto,concluida,funcionario_id,dataCadastro,dataPConclusao) VALUES(?,?,?,?,?,?)";
        
        try (Connection con = ConexaoDB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1,tarefa.getNome());
            ps.setString(2,tarefa.getTexto());
            ps.setBoolean(3,tarefa.getConcluida());
            ps.setInt(4,tarefa.getIdDestinatario());
            ps.setTimestamp(5, Timestamp.valueOf(tarefa.getDataCadastro()));
            ps.setTimestamp(6, Timestamp.valueOf(tarefa.getDataPConclusao()));

            ps.executeUpdate();
        } 
        catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
     
     public List<Tarefa> listar() {

        List<Tarefa> lista = new ArrayList<>();

        String sql = "SELECT nome, texto, concluida, funcionario_id, dataCadastro, dataPConclusao FROM tarefas";

        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Tarefa tarefa = new Tarefa(
                    rs.getString("nome"),
                    rs.getString("texto"),
                    rs.getBoolean("concluida"),
                    rs.getInt("funcionario_id"),
                    rs.getObject("dataCadastro", LocalDateTime.class),
                    rs.getObject("dataPConclusao", LocalDateTime.class)
            );
            lista.add(tarefa);
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return lista;
    }
     
    public void editar(long id, Tarefa tarefa){
        String sql = "UPDATE tarefa SET nome=?, texto=?, concluida=?, funcionario_id=?, dataCadastro=?, dataPConclusao=?, WHERE id=?";

        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, tarefa.getNome());
            ps.setString(2, tarefa.getTexto());
            ps.setBoolean(3, tarefa.getConcluida());
            ps.setInt(4, tarefa.getIdDestinatario());
            ps.setTimestamp(5, Timestamp.valueOf(tarefa.getDataCadastro()));
            ps.setTimestamp(6, Timestamp.valueOf(tarefa.getDataPConclusao()));
            ps.setLong(7, tarefa.getId());

            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }   
    }
    
    public void editarConcluida(long id, boolean concluida){
        String sql = "UPDATE tarefa SET concluida=? WHERE id=?";

        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
        
            ps.setBoolean(1, concluida);
            ps.setLong(2, id);

            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
    
    public void excluir(Tarefa tarefa) {
        Long id = tarefa.getId();
        String sql = "DELETE FROM tarefas WHERE id=?";
        
        try(Connection con = ConexaoDB.getConnection();
          
            PreparedStatement ps = con.prepareStatement(sql)){
  
            ps.setLong(1,id);
            ps.executeUpdate();
        }
         catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
    
    public Tarefa buscar(Long id) { 
        String sql = "SELECT nome=?, texto=?, concluida=?, funcionario_id=?, dataCadastro=?, dataPConclusao=?, WHERE id=?";


        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setLong(1,id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Tarefa tarefa = new Tarefa(
                    rs.getString("nome"),
                    rs.getString("texto"),
                    rs.getBoolean("concluida"),
                    rs.getInt("funcionario_id"),
                    rs.getObject("dataCadastro", LocalDateTime.class),
                    rs.getObject("dataPConclusao", LocalDateTime.class)
                );
                
                return tarefa;
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }   

        return null;
    }
}

