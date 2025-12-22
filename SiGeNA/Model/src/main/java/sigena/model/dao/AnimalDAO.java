package sigena.model.dao;

import java.sql.*;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.util.DataConverter;
import sigena.model.domain.Animal;
import sigena.model.domain.Especie;
import sigena.model.domain.Habitat;
import sigena.model.service.GestaoHabitatService;
import sigena.model.util.ConexaoDB;

public class AnimalDAO {
    public void cadastrar(Animal animal) throws PersistenciaException {
        String sql = "INSERT INTO animais (nome, id_especie, sexo, data_de_nascimento, peso, hostil, data_de_insercao, arquivado) VALUES (?, ?, ?, ?, ?, ?, NOW(), 0)";
        
        try(Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementInsert(stmt, animal);
            stmt.executeUpdate();
            
            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    Long id = rs.getLong(1);
                    animal.setId(id);
                }
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível cadastrar animal: " + e.getMessage());
        }
    }
    
    public List<Animal> listar(String busca, String filtro) throws PersistenciaException{
        String sql = "SELECT "
                + "animais.*, "
                + "habitat_animal.habitat_nome "
                + "FROM animais "
                + "JOIN habitat_animal "
                + "ON animais.id = habitat_animal.animal_id "
                + "WHERE (id LIKE ? OR nome LIKE ?)";
        
        if(filtro != null && !filtro.isEmpty())
            sql += " AND id_especie = ? ";
        
        sql += "AND arquivado = false ORDER BY data_de_insercao ASC;";
        
        List<Animal> animais = new ArrayList<>();
        
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);){
            stmt.setString(1, "%" + busca + "%");
            stmt.setString(2, "%" + busca + "%");
            
            if(filtro != null && !filtro.isEmpty())
                stmt.setString(3, filtro);
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                animais.add(consultaToAnimal(rs));
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível listar animais: " + e.getMessage());
        }
        
        return animais;
    }
    
    public List<Animal> listar() throws PersistenciaException {
        return listar("", "");
    }
    
    public void excluir(Long id) throws PersistenciaException {
        String sql = "UPDATE animais "
                + "SET arquivado = true "
                + "WHERE id = ?;";
        
        try(Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível excluir animal: " + e.getMessage());
        }
    }
    
    public Animal buscarPorId(Long id) throws PersistenciaException {
        String sql = "SELECT "
                + "animais.*, "
                + "habitat_animal.habitat_nome "
                + "FROM animais "
                + "JOIN habitat_animal "
                + "ON animais.id = habitat_animal.animal_id "
                + "WHERE animais.id = ? AND arquivado = false;";
        
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
        String sqlAnimal = "UPDATE animais "
                + "SET nome = ?, "
                + "id_especie = ?, "
                + "sexo = ?, "
                + "data_de_nascimento = ?, "
                + "peso = ?, "
                + "hostil = ? "
                + "WHERE id = ? AND arquivado = false";
        
        try(Connection con = ConexaoDB.getConnection();) {
            try (PreparedStatement stmt = con.prepareStatement(sqlAnimal, Statement.RETURN_GENERATED_KEYS)){
                setPreparedStatementUpdate(stmt, animal);
                stmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível editar animal: " + e.getMessage());
        }
        
        
    }
    
    private Animal consultaToAnimal(ResultSet rs) throws SQLException, PersistenciaException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        EspecieDAO consultaEspecie = new EspecieDAO();
        GestaoHabitatService consultaHabitat = new GestaoHabitatService();
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
        String habitatNome = rs.getString("habitat_nome");
        Habitat habitat = null;

        if (habitatNome != null) {
            habitat = consultaHabitat.buscar(habitatNome);
        }
        
        return new Animal(id, nome, especie, sexo, dataDeNascimento, peso, hostil, habitat);
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
