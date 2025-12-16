package sigena.model.dao;

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
        INSERT INTO ocorrencia (descricao, tipo, status, data)
        VALUES (?, ?, ?, ?)
    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, o.getDescricao());
            ps.setString(2, o.getTipo().name());
            ps.setString(3, o.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(o.getData()));

            ps.executeUpdate();

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
