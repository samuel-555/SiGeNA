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
import java.time.LocalDateTime;
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

        String sql = "SELECT tipo, descricao, data FROM historico WHERE funcionario_id = ? ORDER BY data DESC;";

        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_funcionario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                TipoHistorico tipo = TipoHistorico.valueOf(rs.getString("tipo"));

                Historico historico = new Historico(
                    rs.getString("descricao"),
                    rs.getObject("data", LocalDateTime.class),
                    tipo
                );
                lista.add(historico);
            }

        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

   
    public List<Historico> buscarPorTipo(TipoHistorico tipo) { 
        String sql = "SELECT tipo, descricao, data FROM historico WHERE tipo = ? ORDER BY data DESC;";

        List<Historico> lista = new ArrayList<>();
        
    try (Connection con = ConexaoDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Historico historico = new Historico(
                    rs.getString("descricao"),
                    rs.getObject("data", LocalDateTime.class),
                    TipoHistorico.valueOf(rs.getString("tipo"))
                );
                lista.add(historico);
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }   

        return lista;
    }

    public List<Historico> buscarPorFuncionario(String nome) {

        String sql = """
            SELECT h.tipo, h.descricao, h.data
            FROM historico h
            JOIN funcionarios f ON f.id = h.funcionario_id
            WHERE LOWER(f.nome) LIKE ?
            OR LOWER(f.cargo) LIKE ?
            ORDER BY h.data DESC
        """;

        List<Historico> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            String termoLower = "%" + nome.toLowerCase() + "%";
            ps.setString(1, termoLower);
            ps.setString(2, termoLower); 
            
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                TipoHistorico tipo = TipoHistorico.valueOf(
                    rs.getString("tipo")
                );

                Historico historico = new Historico(
                    rs.getString("descricao"),
                    rs.getObject("data", LocalDateTime.class),
                    tipo
                );

                lista.add(historico);
            }

        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

}
