package sigena.model.dao;

import java.sql.*;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import sigena.model.domain.util.DataConverter;
import sigena.model.domain.Animal;

public class AnimalDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/animal_teste";
    private static final String USUARIO = "root";
    private static final String SENHA = "";
    
    public void cadastrar(Animal animal) {
        String sql = "INSERT INTO animais (nome, sexo, data_de_nascimento, peso, hostil, data_de_insercao) VALUES (?, ?, ?, ?, ?, NOW())";
        try(Connection con = DriverManager.getConnection(URL, USUARIO, SENHA);
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, animal.getNome());
            stmt.setString(2, animal.getSexo());
            
            if(animal.getDataDeNascimentoOb() != null)
                stmt.setDate(3, java.sql.Date.valueOf(animal.getDataDeNascimentoOb()));
            else
                stmt.setNull(3, Types.DATE);
            stmt.setDouble(4, animal.getPeso());
            stmt.setBoolean(5, animal.getHostilidade());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Animal> listar(){
        String sql = "SELECT * FROM animais";
        List<Animal> animais = new ArrayList<>();
        
        try (Connection con = DriverManager.getConnection(URL, USUARIO, SENHA);
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()){
            
            while(rs.next())
                animais.add(consultaToAnimal(rs));
            
        } catch (SQLException e) {
            System.out.println("Erro ao exibir animais: " + e.getMessage());
        }
        
        
        return animais;
    }
    
    public void excluir(String id) {
        String sql = "DELETE FROM animais WHERE id = ?";
        
        try(Connection con = DriverManager.getConnection(URL, USUARIO, SENHA);
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, id);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void exibir(String id) {
        
    }
    
    private Animal consultaToAnimal(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        String sexo = rs.getString("sexo");
        String dataDeNascimento = rs.getDate("data_de_nascimento").toLocalDate().toString();
        Double peso = rs.getDouble("peso");
        boolean hostil = rs.getBoolean("hostil");
        
        return new Animal(id, nome, sexo, dataDeNascimento, peso, hostil);
    }
}
