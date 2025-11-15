/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.dao;

import java.sql.*;

import java.util.List;
import java.util.ArrayList;
import sigena.model.domain.Habitat;
import sigena.model.util.ConexaoDB;

public class HabitatDAO {
    
    public void inserir(Habitat habitat){
        
        String sql = "INSERT INTO habitat(nome,tipo,capacidade,tamanho,manutencao,disponivel) VALUES(?,?,?,?,?,?)";
        
        try (Connection con = ConexaoDB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1,habitat.getNome());
            ps.setString(2,habitat.getTipo());
            ps.setInt(3,habitat.getCapacidade());
            ps.setInt(4,habitat.getTamanho());
            ps.setBoolean(5,habitat.getManutencao());
            ps.setBoolean(6,habitat.getDisponivel());

            ps.executeUpdate();
        } 
        catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
    
    public void editar(String nomeAntigo, Habitat habitat){
    String sql = "UPDATE habitat SET nome=?, tipo=?, tamanho=?, manutencao=?, capacidade=?, disponivel=? WHERE nome=?";

    try(Connection con = ConexaoDB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){

        ps.setString(1, habitat.getNome());
        ps.setString(2, habitat.getTipo());
        ps.setInt(3, habitat.getTamanho());
        ps.setBoolean(4, habitat.getManutencao());
        ps.setInt(5, habitat.getCapacidade());
        ps.setBoolean(6, habitat.getDisponivel());
        ps.setString(7, nomeAntigo);

        ps.executeUpdate();
    }
    catch(SQLException e){
        throw new RuntimeException(e);
    }
}



    
    public void editarCapacidade(String nomeHabitat, int novaCapacidade){
    String sql = "UPDATE habitat SET capacidade=? WHERE nome=?";

    try(Connection con = ConexaoDB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        
        ps.setInt(1, novaCapacidade);
        ps.setString(2, nomeHabitat);

        ps.executeUpdate();
    }
    catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
    
    public void editarManutencao(String nomeHabitat, boolean manutencao){
    String sql = "UPDATE habitat SET manutencao=? WHERE nome=?";

    try(Connection con = ConexaoDB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        
        ps.setBoolean(1, manutencao);
        ps.setString(2, nomeHabitat);

        ps.executeUpdate();
    }
    catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
    
    public void editarDisponivel(String nomeHabitat, boolean disponivel){
    String sql = "UPDATE habitat SET disponivel=? WHERE nome=?";

    try(Connection con = ConexaoDB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
        
        ps.setBoolean(1, disponivel);
        ps.setString(2, nomeHabitat);

        ps.executeUpdate();
    }
    catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
    
    public void excluir(Habitat habitat) {
        String nome = habitat.getNome();
        String sql = "DELETE FROM habitat WHERE nome=?";
        
        try(Connection con = ConexaoDB.getConnection();
          
            PreparedStatement ps = con.prepareStatement(sql)){
  
            ps.setString(1,nome);
            ps.executeUpdate();
        }
         catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
    
    public List<Habitat> listar() {

        List<Habitat> lista = new ArrayList<>();

        String sql = "SELECT tipo, nome, tamanho, manutencao, capacidade, disponivel FROM habitat";

        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Habitat habitat = new Habitat(
                    rs.getString("tipo"),
                    rs.getString("nome"),
                    rs.getInt("tamanho"),
                    rs.getBoolean("manutencao")
            );
            habitat.setCapacidade(rs.getInt("capacidade"));
            habitat.setDisponivel(rs.getBoolean("disponivel"));
            lista.add(habitat);
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return lista;
    }


    
    public Habitat buscar(String nome) { //-------------------ver isso aqui-------------------------
        String sql = "SELECT tipo, nome, tamanho, manutencao, capacidade, disponivel FROM habitat WHERE nome = ?";


        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1,nome);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Habitat habitat = new Habitat(
                        rs.getString("tipo"),
                        rs.getString("nome"),
                        rs.getInt("tamanho"),
                        rs.getBoolean("manutencao")
                );
                habitat.setCapacidade(rs.getInt("capacidade"));
                habitat.setDisponivel(rs.getBoolean("disponivel"));
               
                return habitat;
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }   

        return null;
    }
       
    public void inserirAnimalAlocado(String habitatNome, int animalId) {
        String sql = "INSERT INTO habitat_animal(habitat_nome, animal_id) VALUES(?,?)";
        
        try(Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setString(1,habitatNome);
            ps.setInt(2,animalId);
            
            ps.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }

    }
}
