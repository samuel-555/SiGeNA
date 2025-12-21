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
        String sql = "INSERT INTO eventos (titulo, descricao, data_programada, ocorrido, cancelado, data_de_insercao, arquivado) VALUES (?, ?, ?, 0,0, NOW(), 0)";
        
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
    
    public List<Evento> listar(String busca, String filtro, String tipo, LocalDateTime inicio, LocalDateTime fim) throws PersistenciaException {
        StringBuilder sql = new StringBuilder("SELECT * FROM eventos WHERE arquivado = false ");

        if("ocorridos".equals(tipo)) {

            if("ocorridos".equals(filtro)) {
                sql.append("AND ocorrido = true ");
            } 
            else if("cancelados".equals(filtro)) {
                sql.append("AND cancelado = true AND data_programada < CURRENT_TIMESTAMP ");
            } 
            else {
                sql.append("AND (ocorrido = true OR (cancelado = true AND data_programada < CURRENT_TIMESTAMP)) ");
            }
        }
        else if ("cancelados".equals(tipo)) {
            sql.append("AND cancelado = true AND data_programada > CURRENT_TIMESTAMP ");
        } else {
            sql.append("AND cancelado = false AND ocorrido = false ");
        }
        
        if(inicio != null)
            sql.append("AND data_programada >= ? ");
        
        if(fim != null)
            sql.append("AND data_programada <= ? ");
        
        sql.append("AND titulo like ? ");
        
        sql.append("ORDER BY data_programada ASC");
        
        List<Evento> eventos = new ArrayList<>();
        
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql.toString());){
            
            int index = 1;
            
            if(inicio != null)
                stmt.setObject(index++, inicio);
            
            if(fim != null)
                stmt.setObject(index++, fim);
            
            stmt.setString(index, "%" + busca + "%");
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                eventos.add(consultaToEvento(rs));
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível listar eventos: " + e.getMessage());
        }
        
        return eventos;
    }
    
    public void excluir(Long id) throws PersistenciaException {
        String sql = "UPDATE eventos "
                + "SET arquivado = true "
                + "WHERE id = ?;";
        
        try(Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível excluir evento: " + e.getMessage());
        }
    }
    
    public void cancelar(Long id) throws PersistenciaException {
        String sql = "UPDATE eventos "
                + "SET cancelado = true "
                + "WHERE id = ?;";
        
        try(Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível cancelar evento: " + e.getMessage());
        }
    }
    
    public void ativar(Long id) throws PersistenciaException {
        String sql = "UPDATE eventos "
                + "SET cancelado = false "
                + "WHERE id = ?;";
        
        try(Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível excluir evento: " + e.getMessage());
        }
    }
    
    public void atualizarOcorridos() throws PersistenciaException {
        String sql = "UPDATE eventos "
                + "SET ocorrido = true "
                + "WHERE data_programada < CURRENT_TIMESTAMP "
                + "AND ocorrido = false "
                + "AND cancelado = false "
                + "AND arquivado = false;";
        
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);){
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível atualizar eventos: " + e.getMessage());
        }
    }
    
    public Evento buscarPorId(Long id) throws PersistenciaException {
        String sql = "SELECT * FROM eventos "
           + "WHERE id = ? "
           + "AND arquivado = false;";
        
        Evento evento = null;
        
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                evento = consultaToEvento(rs);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível exibir evento: " + e.getMessage());
        }
        
        return evento;
    }
    
    public void editar(Evento evento) throws PersistenciaException {
        String sqlEvento = "UPDATE eventos "
                + "SET titulo = ?, "
                + "descricao = ?, "
                + "data_programada = ? "
                + "WHERE id = ? "
                + "AND ocorrido = false "
                + "AND arquivado = false;";

        try(Connection con = ConexaoDB.getConnection();) {
            try (PreparedStatement stmt = con.prepareStatement(sqlEvento, Statement.RETURN_GENERATED_KEYS)){
                setPreparedStatementUpdate(stmt, evento);
                stmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível editar evento: " + e.getMessage());
        }
    }
    
    private Evento consultaToEvento(ResultSet rs) throws SQLException, PersistenciaException {
        Long id = rs.getLong("id");
        String titulo = rs.getString("titulo");
        String descricao = rs.getString("descricao");
        String dataProgramada = rs.getTimestamp("data_programada").toLocalDateTime().toString();
        boolean ocorrido = rs.getBoolean("ocorrido");
        String dataInsercao = rs.getTimestamp("data_de_insercao").toLocalDateTime().toString();
        boolean cancelado = rs.getBoolean("cancelado");
        
        return new Evento(id, titulo, descricao, dataProgramada, ocorrido, dataInsercao, cancelado);
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
