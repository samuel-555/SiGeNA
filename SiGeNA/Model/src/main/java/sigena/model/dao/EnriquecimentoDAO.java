package sigena.model.dao;

import sigena.model.domain.Enriquecimento;
import sigena.model.util.ConexaoDB;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EnriquecimentoDAO {

    public int insert(Enriquecimento e, List<String> habitats) throws SQLException {
        String insertEnriq = "INSERT INTO enriquecimento (nome, tipo, especie_destinada, frequencia, observacoes) VALUES (?, ?, ?, ?, ?)";
        String insertMap = "INSERT INTO enriquecimento_habitat (enriquecimento_id, habitat_nome) VALUES (?, ?)";
        try (Connection con = ConexaoDB.getConnection()) {
            try {
                con.setAutoCommit(false);

                int enrichedId;
                try (PreparedStatement ps = con.prepareStatement(insertEnriq, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, e.getNome());
                    ps.setString(2, e.getTipo());
                    ps.setString(3, e.getEspecieDestinada());
                    ps.setString(4, e.getFrequencia());
                    ps.setString(5, e.getObservacoes());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            enrichedId = rs.getInt(1);
                        } else {
                            con.rollback();
                            throw new SQLException("Falha ao obter id de enriquecimento inserido.");
                        }
                    }
                }

                try (PreparedStatement psMap = con.prepareStatement(insertMap)) {
                    for (String habitat : habitats) {
                        psMap.setInt(1, enrichedId);
                        psMap.setString(2, habitat);
                        psMap.addBatch();
                    }
                    psMap.executeBatch();
                }

                con.commit();
                return enrichedId;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<Enriquecimento> findAll() throws SQLException {
        List<Enriquecimento> lista = new ArrayList<>();
        String q = "SELECT id, nome, tipo, especie_destinada, frequencia, observacoes, data_criacao FROM enriquecimento ORDER BY data_criacao DESC";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(q); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Enriquecimento e = new Enriquecimento();
                e.setId(rs.getInt("id"));
                e.setNome(rs.getString("nome"));
                e.setTipo(rs.getString("tipo"));
                e.setEspecieDestinada(rs.getString("especie_destinada"));
                e.setFrequencia(rs.getString("frequencia"));
                e.setObservacoes(rs.getString("observacoes"));
                Timestamp ts = rs.getTimestamp("data_criacao");
                if (ts != null) {
                    e.setDataCriacao(ts.toLocalDateTime());
                }

                List<String> habitats = new ArrayList<>();
                String qHab = "SELECT habitat_nome FROM enriquecimento_habitat WHERE enriquecimento_id = ?";
                try (PreparedStatement psHab = con.prepareStatement(qHab)) {
                    psHab.setInt(1, e.getId());
                    try (ResultSet rsh = psHab.executeQuery()) {
                        while (rsh.next()) {
                            habitats.add(rsh.getString("habitat_nome"));
                        }
                    }
                }
                e.setHabitats(habitats);
                lista.add(e);
            }
        }
        return lista;
    }

    public Enriquecimento findById(int id) throws SQLException {
        String sql = "SELECT id, nome, tipo, especie_destinada, frequencia, observacoes, data_criacao "
                + "FROM enriquecimento WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Enriquecimento e = new Enriquecimento();
                e.setId(rs.getInt("id"));
                e.setNome(rs.getString("nome"));
                e.setTipo(rs.getString("tipo"));
                e.setEspecieDestinada(rs.getString("especie_destinada"));
                e.setFrequencia(rs.getString("frequencia"));
                e.setObservacoes(rs.getString("observacoes"));

                Timestamp ts = rs.getTimestamp("data_criacao");
                if (ts != null) {
                    e.setDataCriacao(ts.toLocalDateTime());
                }

                String sqlHab = "SELECT habitat_nome FROM enriquecimento_habitat WHERE enriquecimento_id = ?";
                try (PreparedStatement psHab = con.prepareStatement(sqlHab)) {
                    psHab.setInt(1, id);
                    try (ResultSet rsh = psHab.executeQuery()) {
                        List<String> habitats = new ArrayList<>();
                        while (rsh.next()) {
                            habitats.add(rsh.getString(1));
                        }
                        e.setHabitats(habitats);
                    }
                }
                return e;
            }
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM enriquecimento WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void update(Enriquecimento e, List<String> habitats) throws SQLException {

        String updateSql = "UPDATE enriquecimento SET nome=?, tipo=?, especie_destinada=?, frequencia=?, observacoes=? WHERE id=?";
        String deleteHab = "DELETE FROM enriquecimento_habitat WHERE enriquecimento_id=?";
        String insertHab = "INSERT INTO enriquecimento_habitat (enriquecimento_id, habitat_nome) VALUES (?, ?)";

        try (Connection con = ConexaoDB.getConnection()) {
            try {
                con.setAutoCommit(false);

                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setString(1, e.getNome());
                    ps.setString(2, e.getTipo());
                    ps.setString(3, e.getEspecieDestinada());
                    ps.setString(4, e.getFrequencia());
                    ps.setString(5, e.getObservacoes());
                    ps.setInt(6, e.getId());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(deleteHab)) {
                    ps.setInt(1, e.getId());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(insertHab)) {
                    for (String h : habitats) {
                        ps.setInt(1, e.getId());
                        ps.setString(2, h);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<String> findAllHabitats() throws SQLException {
        List<String> lista = new ArrayList<>();
        String q = "SELECT nome FROM habitat ORDER BY nome";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(q); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("nome"));
            }
        }
        return lista;
    }
}
