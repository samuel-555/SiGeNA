package sigena.model.dao;

import sigena.model.domain.util.OcorrenciaTipo;
import sigena.model.domain.util.StatusOcorrencia;
import sigena.model.domain.*;
import java.sql.*;
import java.util.*;

public class OcorrenciaDAO {

    private final Connection con;

    public OcorrenciaDAO(Connection con) {
        this.con = con;
    }

    public void criar(Ocorrencia o) {
        String sql = """
        INSERT INTO ocorrencia (descricao, tipo, status, data, cpf_cadastrador)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = con.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, o.getDescricao());
            ps.setString(2, o.getTipo().name());
            ps.setString(3, o.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(o.getData()));
            ps.setString(5, o.getCpfCadastrador());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                o.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar ocorrência.", e);
        }
    }

    public List<Ocorrencia> listar() {
        List<Ocorrencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM ocorrencia \n" + "WHERE status <> 'CANCELADA'\n" + "ORDER BY data DESC";

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ocorrencia o = new Ocorrencia();
                o.setId(rs.getLong("id"));
                o.setDescricao(rs.getString("descricao"));
                o.setTipo(OcorrenciaTipo.valueOf(rs.getString("tipo")));
                o.setStatus(StatusOcorrencia.valueOf(rs.getString("status")));
                o.setData(rs.getTimestamp("data").toLocalDateTime());

                lista.add(o);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar ocorrências.", e);
        }

        return lista;
    }

    public List<Ocorrencia> buscarComFiltro(String tipo, String status, String texto) {

        StringBuilder sql = new StringBuilder("""
        SELECT * FROM ocorrencia
        WHERE status <> 'CANCELADA'
    """);

        List<Object> params = new ArrayList<>();

        if (tipo != null && !tipo.isBlank()) {
            sql.append(" AND tipo = ?");
            params.add(tipo);
        }

        if (status != null && !status.isBlank()) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        if (texto != null && !texto.isBlank()) {
            sql.append(" AND descricao LIKE ?");
            params.add("%" + texto + "%");
        }

        sql.append(" ORDER BY data DESC");

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            List<Ocorrencia> lista = new ArrayList<>();

            while (rs.next()) {
                Ocorrencia o = new Ocorrencia();
                o.setId(rs.getLong("id"));
                o.setDescricao(rs.getString("descricao"));
                o.setTipo(OcorrenciaTipo.valueOf(rs.getString("tipo")));
                o.setStatus(StatusOcorrencia.valueOf(rs.getString("status")));
                o.setData(rs.getTimestamp("data").toLocalDateTime());
                lista.add(o);
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ocorrências.", e);
        }
    }

    public Ocorrencia buscarPorId(Long id) {
        String sql = "SELECT * FROM ocorrencia WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ocorrencia o = new Ocorrencia();
                    o.setId(rs.getLong("id"));
                    o.setDescricao(rs.getString("descricao"));
                    o.setTipo(OcorrenciaTipo.valueOf(rs.getString("tipo")));
                    o.setStatus(StatusOcorrencia.valueOf(rs.getString("status")));
                    o.setData(rs.getTimestamp("data").toLocalDateTime());
                    return o;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ocorrência.", e);
        }

        return null;
    }

    public void atualizar(Ocorrencia o) {
        String sql = """
        UPDATE ocorrencia SET
            descricao = ?,
            tipo = ?,
            status = ?,
            data = ?
        WHERE id = ?
    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, o.getDescricao());
            ps.setString(2, o.getTipo().name());
            ps.setString(3, o.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(o.getData()));
            ps.setLong(5, o.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar ocorrência.", e);
        }
    }

    public void cancelar(Long id) {
        String sql = "UPDATE ocorrencia SET status = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, StatusOcorrencia.CANCELADA.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cancelar ocorrência.", e);
        }
    }

}
