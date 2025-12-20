/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sigena.model.dao;

import java.sql.Connection;
import java.util.Date;
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
        
        String sql = "INSERT INTO historico(funcionarioCpf, tipo, descricao, data) VALUES(?,?,?,?)";
        
        try (Connection con = ConexaoDB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1,historico.getFuncionarioCpf());
            ps.setString(2,historico.getTipo().name());
            ps.setString(3,historico.getDescricao());
            ps.setTimestamp(4, new Timestamp(historico.getData().getTime()));


            ps.executeUpdate();
        } 
        catch(SQLException e){
            throw new RuntimeException(e);
        } 
    }
    
    public List<Historico> listarTodos() {

    String sql = """
        SELECT h.funcionarioCpf,
               f.nome AS nomeFuncionario,
               f.cargo,
               h.tipo,
               h.descricao,
               h.data
        FROM historico h
        JOIN funcionarios f ON f.cpf = h.funcionarioCpf
        ORDER BY f.nome, h.data DESC
    """;

    List<Historico> lista = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Historico h = new Historico(
                    rs.getString("funcionarioCpf"),
                    rs.getString("descricao"),
                    rs.getObject("data", Date.class),
                    TipoHistorico.valueOf(rs.getString("tipo"))
                );

                h.setNomeFuncionario(rs.getString("nomeFuncionario"));
                h.setCargoFuncionario(rs.getString("cargo"));

                lista.add(h);
            }
        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public List<Historico> listarPorFuncionario(String funcionarioCpf) {

        List<Historico> lista = new ArrayList<>();

        String sql = "SELECT tipo, descricao, data FROM historico WHERE funcionarioCpf = ? ORDER BY data DESC;";

        try (Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, funcionarioCpf);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                TipoHistorico tipo = TipoHistorico.valueOf(rs.getString("tipo"));

                Historico historico = new Historico(
                    funcionarioCpf,
                    rs.getString("descricao"),
                    rs.getObject("data", Date.class),
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
        String sql = "SELECT h.funcionarioCpf,\n" +
        "h.tipo,\n" +
        "h.descricao,\n" +
        "h.data,\n" +
        "f.nome,\n" +
        "f.cargo\n" +
        "FROM historico h\n" +
        "JOIN funcionarios f ON f.cpf = h.funcionarioCpf\n" +
        "WHERE h.tipo = ?\n" +
        "ORDER BY h.data DESC;";

        List<Historico> lista = new ArrayList<>();
        
    try (Connection con = ConexaoDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Historico historico = new Historico(
                    rs.getString("funcionarioCpf"),
                    rs.getString("descricao"),
                    rs.getObject("data", Date.class),
                    TipoHistorico.valueOf(rs.getString("tipo"))
                );
                historico.setNomeFuncionario(rs.getString("nome"));
                historico.setCargoFuncionario(rs.getString("cargo"));
                
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
            SELECT h.funcionarioCpf,
                   h.tipo,
                   h.descricao,
                   h.data,
                   f.nome,
                   f.cargo
            FROM historico h
            JOIN funcionarios f ON f.cpf = h.funcionarioCpf
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
                    rs.getString("funcionarioCpf"),
                    rs.getString("descricao"),
                    rs.getObject("data", Date.class),
                    tipo
                );
                historico.setNomeFuncionario(rs.getString("nome"));
                historico.setCargoFuncionario(rs.getString("cargo"));
                
                lista.add(historico);
            }

        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

}
