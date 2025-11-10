package sigena.model.dao;

import java.sql.*;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.util.DataConverter;
import sigena.model.domain.Animal;
import sigena.model.domain.Especie;
import sigena.model.util.ConexaoDB;

public class AnimalDAO {
    public void cadastrar(Animal animal) throws PersistenciaException {
        String sql = "INSERT INTO animais (nome, id_especie, sexo, data_de_nascimento, peso, hostil, data_de_insercao) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        try(Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementInsert(stmt, animal);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível cadastrar animal: " + e.getMessage());
        }
    }
    
    public List<Animal> listar() throws PersistenciaException{
        String sql = "SELECT * FROM animais";
        List<Animal> animais = new ArrayList<>();
        
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()){
            while(rs.next())
                animais.add(consultaToAnimal(rs));
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível listar animais: " + e.getMessage());
        }
        
        return animais;
    }
    
    public void excluir(Long id) throws PersistenciaException {
        String sql = "DELETE FROM animais WHERE id = ?";
        
        try(Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível excluir animal: " + e.getMessage());
        }
    }
    
    public Animal buscarPorId(Long id) throws PersistenciaException {
        String sql = "SELECT * FROM animais WHERE id = ?";
        Animal animal = null;
        
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                animal = consultaToAnimal(rs);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível exibir animal: " + e.getMessage());
        }
        
        return animal;
    }
    
    public void editar(Animal animal) throws PersistenciaException {
        String sql = "UPDATE animais "
                + "SET nome = ?, "
                + "id_especie = ?, "
                + "sexo = ?, "
                + "data_de_nascimento = ?, "
                + "peso = ?, "
                + "hostil = ? "
                + "WHERE id = ?";
        
        try(Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementUpdate(stmt, animal);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível editar animal: " + e.getMessage());
        }
    }
    
    private Animal consultaToAnimal(ResultSet rs) throws SQLException, PersistenciaException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        EspecieDAO consultaEspecie = new EspecieDAO();
        Especie especie = null;
        
        try {
            especie = consultaEspecie.buscarPorId(rs.getInt("id_especie"));
        } catch(PersistenciaException e) {
            throw new PersistenciaException(e.getMessage());
        }
        
        String sexo = rs.getString("sexo");
        String dataDeNascimento = rs.getDate("data_de_nascimento").toLocalDate().toString();
        Double peso = rs.getDouble("peso");
        boolean hostil = rs.getBoolean("hostil");
        
        return new Animal(id, nome, especie, sexo, dataDeNascimento, peso, hostil);
    }
    
    private void setPreparedStatementInsert(PreparedStatement stmt, Animal animal) throws SQLException{
        stmt.setString(1, animal.getNome());
            stmt.setInt(2, animal.getEspecieId());
            stmt.setString(3, animal.getSexo());
            
            if(animal.getDataDeNascimentoOb() != null)
                stmt.setDate(4, java.sql.Date.valueOf(animal.getDataDeNascimentoOb()));
            else
                stmt.setNull(4, Types.DATE);
            stmt.setDouble(5, animal.getPeso());
            stmt.setBoolean(6, animal.getHostilidade());
    }
    
    private void setPreparedStatementUpdate(PreparedStatement stmt, Animal animal) throws SQLException{
        setPreparedStatementInsert(stmt, animal);
        stmt.setLong(7, animal.getId());
    }
}
