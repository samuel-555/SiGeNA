package sigena.model.dao;

import sigena.model.common.exception.DatabaseException;
import sigena.model.domain.ReciboDoacao;
import sigena.model.util.ConexaoDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReciboDoacaoDAO {

    // ============================================
    // SALVAR RECIBO
    // ============================================
    public void salvar(ReciboDoacao recibo) throws DatabaseException {
        String sql = """
            INSERT INTO recibo_doacao (doacao_id, codigo, data_emissao)
            VALUES (?, ?, ?);
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, recibo.getDoacaoId());
            ps.setString(2, recibo.getCodigo());
            ps.setTimestamp(3, Timestamp.valueOf(recibo.getDataEmissao()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    recibo.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar recibo: " + e.getMessage());
        }
    }

    // ============================================
    // BUSCAR POR DOAÇÃO
    // ============================================
    public ReciboDoacao buscarPorDoacao(Long doacaoId) throws DatabaseException {
        String sql = "SELECT * FROM recibo_doacao WHERE doacao_id=? ORDER BY data_emissao DESC LIMIT 1;";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, doacaoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRecibo(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar recibo: " + e.getMessage());
        }

        return null;
    }

    // ============================================
    // BUSCAR ÚLTIMO RECIBO DE UMA DOAÇÃO (usado pelo serviço)
    // ============================================
    public ReciboDoacao buscarUltimoPorDoacao(Long doacaoId) throws DatabaseException {
        String sql = """
            SELECT * FROM recibo_doacao
            WHERE doacao_id=?
            ORDER BY data_emissao DESC
            LIMIT 1;
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, doacaoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRecibo(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar último recibo: " + e.getMessage());
        }

        return null;
    }

    // ============================================
    // MÉTODO emitirRecibo — usado pelo GestaoDoacaoService
    // ============================================
    public void emitirRecibo(Long doacaoId) throws DatabaseException {
        String sql = """
            INSERT INTO recibo_doacao (doacao_id, data_emissao)
            VALUES (?, ?);
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, doacaoId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao emitir recibo: " + e.getMessage());
        }
    }

    // ============================================
    // LISTAR TODOS
    // ============================================
    public List<ReciboDoacao> listarTodos() throws DatabaseException {
        List<ReciboDoacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM recibo_doacao ORDER BY data_emissao DESC;";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRecibo(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar recibos: " + e.getMessage());
        }

        return lista;
    }

    // ============================================
    // MAPEAR RESULTSET → OBJETO
    // ============================================
    private ReciboDoacao mapRecibo(ResultSet rs) throws SQLException {
        ReciboDoacao r = new ReciboDoacao();
        r.setId(rs.getLong("id"));
        r.setDoacaoId(rs.getLong("doacao_id"));
        r.setCodigo(rs.getString("codigo"));
        Timestamp ts = rs.getTimestamp("data_emissao");
        if (ts != null) {
            r.setDataEmissao(ts.toLocalDateTime());
        }
        return r;
    }
}
