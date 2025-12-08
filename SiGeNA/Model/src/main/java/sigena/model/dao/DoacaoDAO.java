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
import sigena.model.domain.Doacao;
import sigena.model.domain.DoacaoTipo;
import sigena.model.domain.StatusDoacao;
import sigena.model.util.ConexaoDB;

public class DoacaoDAO {

    public void salvar(Doacao doacao) throws DatabaseException {

        String sql = """
            INSERT INTO doacoes 
            (nome_doador, tipo, valor_monetario, descricao_outro, data_doacao, observacoes, status, recibo_emitido) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, doacao.getNomeDoador());
            ps.setString(2, doacao.getTipo().name());

            if (doacao.getValorMonetario() != null) {
                ps.setDouble(3, doacao.getValorMonetario());
            } else {
                ps.setNull(3, Types.DECIMAL);
            }

            ps.setString(4, doacao.getDescricaoOutro());

            if (doacao.getDataDoacao() != null) {
                ps.setDate(5, java.sql.Date.valueOf(doacao.getDataDoacao()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setString(6, doacao.getObservacoes());
            ps.setString(7, doacao.getStatus().name());
            ps.setBoolean(8, doacao.isReciboEmitido());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    doacao.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar doação: " + e.getMessage());
        }
    }

    public List<Doacao> listarTodas() throws DatabaseException {

        List<Doacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM doacoes ORDER BY data_doacao DESC, id DESC";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar doações: " + e.getMessage());
        }

        return lista;
    }

    public Doacao buscarPorId(Long id) throws DatabaseException {

        String sql = "SELECT * FROM doacoes WHERE id=?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar doação: " + e.getMessage());
        }

        return null;
    }

    public void atualizar(Doacao doacao) throws DatabaseException {

        String sql = """
            UPDATE doacoes 
            SET nome_doador=?, tipo=?, valor_monetario=?, descricao_outro=?, 
                data_doacao=?, observacoes=?, status=?, recibo_emitido=? 
            WHERE id=?;
        """;

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, doacao.getNomeDoador());
            ps.setString(2, doacao.getTipo().name());

            if (doacao.getValorMonetario() != null) {
                ps.setDouble(3, doacao.getValorMonetario());
            } else {
                ps.setNull(3, Types.DECIMAL);
            }

            ps.setString(4, doacao.getDescricaoOutro());

            if (doacao.getDataDoacao() != null) {
                ps.setDate(5, java.sql.Date.valueOf(doacao.getDataDoacao()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setString(6, doacao.getObservacoes());
            ps.setString(7, doacao.getStatus().name());
            ps.setBoolean(8, doacao.isReciboEmitido());

            ps.setLong(9, doacao.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar doação: " + e.getMessage());
        }
    }

    public void atualizarValor(Long id, Double valor) throws DatabaseException {
        String sql = "UPDATE doacoes SET valor_monetario=? WHERE id=?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, valor);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar valor da doação: " + e.getMessage());
        }
    }

    public void atualizarDescricao(Long id, String descricao) throws DatabaseException {
        String sql = "UPDATE doacoes SET descricao_outro=? WHERE id=?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, descricao);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar descrição da doação: " + e.getMessage());
        }
    }

    public void atualizarStatus(Long id, StatusDoacao status) throws DatabaseException {

        String sql = "UPDATE doacoes SET status=? WHERE id=?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar status: " + e.getMessage());
        }
    }

    public void atualizarReciboEmitido(Long id, boolean emitido) throws DatabaseException {

        String sql = "UPDATE doacoes SET recibo_emitido=? WHERE id=?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, emitido);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar recibo_emitido: " + e.getMessage());
        }
    }

    private Doacao mapear(ResultSet rs) throws SQLException {

        Doacao d = new Doacao();

        d.setId(rs.getLong("id"));
        d.setNomeDoador(rs.getString("nome_doador"));
        d.setTipo(DoacaoTipo.fromString(rs.getString("tipo")));

        double valor = rs.getDouble("valor_monetario");
        if (!rs.wasNull()) {
            d.setValorMonetario(valor);
        }

        d.setDescricaoOutro(rs.getString("descricao_outro"));

        if (rs.getDate("data_doacao") != null) {
            d.setDataDoacao(rs.getDate("data_doacao").toLocalDate());
        }

        d.setObservacoes(rs.getString("observacoes"));
        d.setStatus(StatusDoacao.fromString(rs.getString("status")));
        d.setReciboEmitido(rs.getBoolean("recibo_emitido"));

        Timestamp ts = rs.getTimestamp("data_registro");
        if (ts != null) {
            d.setDataRegistro(ts.toLocalDateTime());
        }

        return d;
    }
}
