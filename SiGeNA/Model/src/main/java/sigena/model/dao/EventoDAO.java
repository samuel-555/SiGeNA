package sigena.model.dao;

import java.sql.*;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.util.DataConverter;
import sigena.model.domain.Evento;
import sigena.model.service.GestaoHabitatService;
import sigena.model.util.ConexaoDB;

public class EventoDAO {
    public void cadastrar(Evento evento) throws PersistenciaException {
        String sql = "INSERT INTO eventos (titulo, descricao, data_programada, ocorrido, data_de_insercao, arquivado) VALUES (?, ?, ?, 0, NOW(), 0)";
        
        try(Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementInsert(stmt, evento);
            stmt.executeUpdate();
            
            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    Long id = rs.getLong(1);
                    evento.setId(id);
                }
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível cadastrar evento: " + e.getMessage());
        }
    }
    
    public List<Evento> listar() throws PersistenciaException {
        String sql = "SELECT * FROM eventos ORDER BY data_programada ASC;";
        
        List<Evento> eventos = new ArrayList<>();
        
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);){
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                eventos.add(consultaToEvento(rs));
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível listar eventos: " + e.getMessage());
        }
        
        return eventos;
    }
    
    private Evento consultaToEvento(ResultSet rs) throws SQLException, PersistenciaException {
        Long id = rs.getLong("id");
        String titulo = rs.getString("titulo");
        String descricao = rs.getString("descricao");
        String dataProgramada = rs.getTimestamp("data_programada").toLocalDateTime().toString();
        boolean ocorrido = rs.getBoolean("ocorrido");
        String dataInsercao = rs.getTimestamp("data_de_insercao").toLocalDateTime().toString();
        
        return new Evento(id, titulo, descricao, dataProgramada, ocorrido, dataInsercao);
    }
    
    private void setPreparedStatementInsert(PreparedStatement stmt, Evento evento) throws SQLException{
        stmt.setString(1, evento.getTitulo());
        stmt.setString(2, evento.getDescricao());
        stmt.setObject(3, evento.getDataProgramada());
    }
    
    private void setPreparedStatementUpdate(PreparedStatement stmt, Evento evento) throws SQLException{
        setPreparedStatementInsert(stmt, evento);
        stmt.setLong(4, evento.getId());
    }
}
