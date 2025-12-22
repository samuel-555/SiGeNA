package sigena.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Agendamento;
import sigena.model.domain.AgendamentoStatus;
import sigena.model.util.ConexaoDB;

public class AgendamentoDAO {

    public void inserir(Agendamento agendamento) throws PersistenciaException {
        String sql = """
            INSERT INTO agendamentos
                (tipo, data_agendamento, hora_agendamento, responsavel, local, observacoes, status, criado_em)
            VALUES
                (?, ?, ?, ?, ?, ?, ?, NOW())
            """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setCommonFields(stmt, agendamento);
            stmt.setString(7, agendamento.getStatus().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    agendamento.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel salvar o agendamento: " + e.getMessage());
        }
    }

    public List<Agendamento> listar() throws PersistenciaException {
        String sql = """
            SELECT *
            FROM agendamentos
            ORDER BY criado_em DESC
            """;

        List<Agendamento> agendamentos = new ArrayList<>();
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                agendamentos.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel listar agendamentos: " + e.getMessage());
        }
        return agendamentos;
    }

    public Agendamento buscarPorId(Long id) throws PersistenciaException {
        String sql = """
            SELECT *
            FROM agendamentos
            WHERE id = ?
            """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel buscar agendamento: " + e.getMessage());
        }
        return null;
    }

    public void atualizar(Agendamento agendamento) throws PersistenciaException {
        String sql = """
            UPDATE agendamentos
            SET tipo = ?, data_agendamento = ?, hora_agendamento = ?, responsavel = ?, local = ?, observacoes = ?
            WHERE id = ?
            """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            setCommonFields(stmt, agendamento);
            stmt.setLong(7, agendamento.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel atualizar agendamento: " + e.getMessage());
        }
    }

    public void cancelar(Long id, String justificativa) throws PersistenciaException {
        String sql = """
            UPDATE agendamentos
            SET status = ?, cancelado_em = NOW(), justificativa_cancelamento = ?
            WHERE id = ?
            """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, AgendamentoStatus.CANCELADO.name());
            if (justificativa == null || justificativa.isBlank()) {
                stmt.setNull(2, Types.LONGVARCHAR);
            } else {
                stmt.setString(2, justificativa);
            }
            stmt.setLong(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel cancelar o agendamento: " + e.getMessage());
        }
    }

    public boolean existeConflito(LocalDate data, LocalTime hora, String responsavel, String local, Long idIgnorado) throws PersistenciaException {
        String sql = """
            SELECT COUNT(1)
            FROM agendamentos
            WHERE data_agendamento = ?
                AND hora_agendamento = ?
                AND status = 'ATIVO'
                AND (responsavel = ? OR local = ?)
            """;

        if (idIgnorado != null) {
            sql += " AND id <> ?";
        }

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(data));
            stmt.setTime(2, java.sql.Time.valueOf(hora));
            stmt.setString(3, responsavel);
            stmt.setString(4, local);
            if (idIgnorado != null) {
                stmt.setLong(5, idIgnorado);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Falha ao verificar conflito de horario: " + e.getMessage());
        }
        return false;
    }

    private void setCommonFields(PreparedStatement stmt, Agendamento agendamento) throws SQLException {
        stmt.setString(1, agendamento.getTipo());
        stmt.setDate(2, java.sql.Date.valueOf(agendamento.getData()));
        stmt.setTime(3, java.sql.Time.valueOf(agendamento.getHora()));
        stmt.setString(4, agendamento.getResponsavel());
        stmt.setString(5, agendamento.getLocal());

        if (agendamento.getObservacoes() == null || agendamento.getObservacoes().isBlank()) {
            stmt.setNull(6, Types.LONGVARCHAR);
        } else {
            stmt.setString(6, agendamento.getObservacoes());
        }
    }

    private Agendamento mapear(ResultSet rs) throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(rs.getLong("id"));
        agendamento.setTipo(rs.getString("tipo"));
        agendamento.setData(rs.getDate("data_agendamento").toLocalDate());
        agendamento.setHora(rs.getTime("hora_agendamento").toLocalTime());
        agendamento.setResponsavel(rs.getString("responsavel"));
        agendamento.setLocal(rs.getString("local"));
        agendamento.setObservacoes(rs.getString("observacoes"));
        agendamento.setStatus(AgendamentoStatus.valueOf(rs.getString("status")));
        agendamento.setJustificativaCancelamento(rs.getString("justificativa_cancelamento"));

        Timestamp criadoEm = rs.getTimestamp("criado_em");
        if (criadoEm != null) {
            agendamento.setCriadoEm(criadoEm.toLocalDateTime());
        }

        Timestamp canceladoEm = rs.getTimestamp("cancelado_em");
        if (canceladoEm != null) {
            agendamento.setCanceladoEm(canceladoEm.toLocalDateTime());
        }

        return agendamento;
    }
}
