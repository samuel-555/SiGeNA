package sigena.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import sigena.model.common.exception.DatabaseException;
import sigena.model.domain.Visita;
import sigena.model.util.ConexaoDB;

public class VisitaDAO {

    public void salvar(Visita visita) throws DatabaseException {
        String sql = """
            INSERT INTO visitas (nome_visitante, documento, motivo, data_visita, observacoes, vip, necessidade_especial, descricao_necessidade, turno, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, visita.getNomeVisitante());
            ps.setString(2, visita.getDocumento());
            ps.setString(3, visita.getMotivo());
            ps.setDate(4, visita.getDataVisita() != null ? java.sql.Date.valueOf(visita.getDataVisita()) : null);
            if (visita.getObservacoes() == null || visita.getObservacoes().isBlank()) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, visita.getObservacoes());
            }
            ps.setBoolean(6, visita.isVip());
            ps.setBoolean(7, visita.isNecessidadeEspecial());
            if (visita.getDescricaoNecessidade() == null || visita.getDescricaoNecessidade().isBlank()) {
                ps.setNull(8, Types.VARCHAR);
            } else {
                ps.setString(8, visita.getDescricaoNecessidade());
            }
            if (visita.getTurno() != null) {
                ps.setString(9, visita.getTurno().name());
            } else {
                ps.setNull(9, Types.VARCHAR);
            }
            ps.setString(10, "ATIVA");

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    visita.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar visita: " + e.getMessage());
        }
    }

    public void atualizar(Visita visita) throws DatabaseException {
        String sql = """
            UPDATE visitas
            SET nome_visitante=?, documento=?, motivo=?, data_visita=?, observacoes=?, vip=?, necessidade_especial=?, descricao_necessidade=?, turno=?
            WHERE id=?
            """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, visita.getNomeVisitante());
            ps.setString(2, visita.getDocumento());
            ps.setString(3, visita.getMotivo());
            ps.setDate(4, visita.getDataVisita() != null ? java.sql.Date.valueOf(visita.getDataVisita()) : null);
            if (visita.getObservacoes() == null || visita.getObservacoes().isBlank()) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, visita.getObservacoes());
            }
            ps.setBoolean(6, visita.isVip());
            ps.setBoolean(7, visita.isNecessidadeEspecial());
            if (visita.getDescricaoNecessidade() == null || visita.getDescricaoNecessidade().isBlank()) {
                ps.setNull(8, Types.VARCHAR);
            } else {
                ps.setString(8, visita.getDescricaoNecessidade());
            }
            if (visita.getTurno() != null) {
                ps.setString(9, visita.getTurno().name());
            } else {
                ps.setNull(9, Types.VARCHAR);
            }
            ps.setLong(10, visita.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar visita: " + e.getMessage());
        }
    }

    public void excluir(Long id) throws DatabaseException {
        String sql = "UPDATE visitas SET status='CANCELADA' WHERE id=?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao excluir visita: " + e.getMessage());
        }
    }

    public Visita buscarPorId(Long id) throws DatabaseException {
        String sql = "SELECT * FROM visitas WHERE id=?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar visita: " + e.getMessage());
        }
        return null;
    }

    public List<Visita> listar(String ordenacao, LocalDate inicio, LocalDate fim, String buscaTexto) throws DatabaseException {
        List<Visita> visitas = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM visitas WHERE 1=1 AND (status IS NULL OR UPPER(status) <> 'CANCELADA')");

        if (inicio != null) {
            sql.append(" AND data_visita >= ?");
        }
        if (fim != null) {
            sql.append(" AND data_visita <= ?");
        }
        if (buscaTexto != null && !buscaTexto.isBlank()) {
            sql.append(" AND (LOWER(nome_visitante) LIKE ? OR LOWER(documento) LIKE ?)");
        }

        if ("antigas".equalsIgnoreCase(ordenacao)) {
            sql.append(" ORDER BY data_visita ASC, id ASC");
        } else {
            sql.append(" ORDER BY data_visita DESC, id DESC");
        }

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;
            if (inicio != null) {
                ps.setDate(idx++, java.sql.Date.valueOf(inicio));
            }
            if (fim != null) {
                ps.setDate(idx++, java.sql.Date.valueOf(fim));
            }
            if (buscaTexto != null && !buscaTexto.isBlank()) {
                String like = "%" + buscaTexto.toLowerCase() + "%";
                ps.setString(idx++, like);
                ps.setString(idx, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    visitas.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar visitas: " + e.getMessage());
        }
        return visitas;
    }

    public long contarTodas() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM visitas WHERE status IS NULL OR UPPER(status) <> 'CANCELADA'";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar visitas: " + e.getMessage());
        }
        return 0;
    }

    public long contarHoje() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM visitas WHERE data_visita = CURRENT_DATE AND (status IS NULL OR UPPER(status) <> 'CANCELADA')";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar visitas de hoje: " + e.getMessage());
        }
        return 0;
    }

    private Visita mapear(ResultSet rs) throws SQLException {
        Visita v = new Visita();
        v.setId(rs.getLong("id"));
        v.setNomeVisitante(rs.getString("nome_visitante"));
        v.setDocumento(rs.getString("documento"));
        v.setMotivo(rs.getString("motivo"));
        if (rs.getDate("data_visita") != null) {
            v.setDataVisita(rs.getDate("data_visita").toLocalDate());
        }
        v.setObservacoes(rs.getString("observacoes"));
        v.setVip(rs.getBoolean("vip"));
        v.setNecessidadeEspecial(rs.getBoolean("necessidade_especial"));
        v.setDescricaoNecessidade(rs.getString("descricao_necessidade"));
        v.setStatus(rs.getString("status"));
        String turnoStr = rs.getString("turno");
        if (turnoStr != null && !turnoStr.isBlank()) {
            try {
                v.setTurno(sigena.model.domain.Turno.valueOf(turnoStr));
            } catch (IllegalArgumentException ignored) {
            }
        }
        Timestamp ts = rs.getTimestamp("data_registro");
        if (ts != null) {
            v.setDataRegistro(ts.toLocalDateTime());
        }
        return v;
    }
}
