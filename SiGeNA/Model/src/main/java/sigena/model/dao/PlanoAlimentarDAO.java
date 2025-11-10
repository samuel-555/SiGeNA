package sigena.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Animal;
import sigena.model.domain.ItemPlanoAlimentar;
import sigena.model.domain.PlanoAlimentar;
import sigena.model.util.ConexaoDB;

public class PlanoAlimentarDAO {

    private static final String SQL_INSERIR_PLANO = """
            INSERT INTO planos_alimentares (animal_id, data_criacao)
            VALUES (?, NOW())
            """;

    private static final String SQL_INSERIR_ITEM = """
            INSERT INTO itens_plano_alimentar (plano_id, alimento, gramatura, vezes_por_dia)
            VALUES (?, ?, ?, ?)
            """;

    private static final String SQL_ATUALIZAR_PLANO = """
            UPDATE planos_alimentares SET animal_id = ? WHERE id = ?
            """;

    private static final String SQL_EXCLUIR_ITENS = """
            DELETE FROM itens_plano_alimentar WHERE plano_id = ?
            """;

    private static final String SQL_LISTAR_PLANOS = """
            SELECT id, animal_id, data_criacao
            FROM planos_alimentares
            ORDER BY id DESC
            """;

    private static final String SQL_BUSCAR_PLANO = """
            SELECT id, animal_id, data_criacao
            FROM planos_alimentares
            WHERE id = ?
            """;

    private static final String SQL_BUSCAR_ITENS = """
            SELECT id, alimento, gramatura, vezes_por_dia
            FROM itens_plano_alimentar
            WHERE plano_id = ?
            ORDER BY id
            """;

    private static final String SQL_EXCLUIR_PLANO = """
            DELETE FROM planos_alimentares WHERE id = ?
            """;

    private final AnimalDAO animalDAO = new AnimalDAO();

    public void inserir(PlanoAlimentar plano) throws PersistenciaException {
        validarPlano(plano);
        try (Connection con = ConexaoDB.getConnection()) {
            boolean autoCommitAnterior = con.getAutoCommit();
            try {
                con.setAutoCommit(false);
                Long id = inserirPlano(plano, con);
                inserirItens(plano.getItens(), id, con);
                con.commit();
            } catch (SQLException e) {
                rollbackSilencioso(con);
                throw new PersistenciaException("Erro ao cadastrar plano alimentar: " + e.getMessage());
            } finally {
                restaurarAutoCommit(con, autoCommitAnterior);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao cadastrar plano alimentar: " + e.getMessage());
        }
    }

    public void atualizar(PlanoAlimentar plano) throws PersistenciaException {
        validarPlano(plano);
        if (plano.getId() == null) {
            throw new PersistenciaException("Plano sem identificador.");
        }

        try (Connection con = ConexaoDB.getConnection()) {
            boolean autoCommitAnterior = con.getAutoCommit();
            try {
                con.setAutoCommit(false);
                atualizarPlano(plano, con);
                limparItens(plano.getId(), con);
                inserirItens(plano.getItens(), plano.getId(), con);
                con.commit();
            } catch (SQLException e) {
                rollbackSilencioso(con);
                throw new PersistenciaException("Erro ao atualizar plano alimentar: " + e.getMessage());
            } finally {
                restaurarAutoCommit(con, autoCommitAnterior);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar plano alimentar: " + e.getMessage());
        }
    }

    public void excluir(Long id) throws PersistenciaException {
        if (id == null) {
            throw new PersistenciaException("Identificador do plano é obrigatório.");
        }
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_EXCLUIR_PLANO)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao excluir plano alimentar: " + e.getMessage());
        }
    }

    public List<PlanoAlimentar> listar() throws PersistenciaException {
        List<PlanoAlimentar> planos = new ArrayList<>();
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_PLANOS);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PlanoAlimentar plano = mapearPlano(rs);
                plano.setItens(buscarItens(plano.getId(), con));
                planos.add(plano);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar planos alimentares: " + e.getMessage());
        }
        return planos;
    }

    public PlanoAlimentar buscarPorId(Long id) throws PersistenciaException {
        if (id == null) {
            throw new PersistenciaException("Identificador do plano é obrigatório.");
        }
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_PLANO)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PlanoAlimentar plano = mapearPlano(rs);
                    plano.setItens(buscarItens(plano.getId(), con));
                    return plano;
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar plano alimentar: " + e.getMessage());
        }
        return null;
    }

    private Long inserirPlano(PlanoAlimentar plano, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERIR_PLANO, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, plano.getAnimal().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    plano.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Falha ao obter o ID gerado para o plano alimentar.");
    }

    private void inserirItens(List<ItemPlanoAlimentar> itens, Long planoId, Connection con) throws SQLException {
        if (itens == null || itens.isEmpty()) {
            return;
        }
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERIR_ITEM)) {
            boolean possuiItensValidos = false;
            for (ItemPlanoAlimentar item : itens) {
                if (item.getAlimento() == null || item.getAlimento().isBlank()) {
                    continue;
                }
                ps.setLong(1, planoId);
                ps.setString(2, item.getAlimento());
                if (item.getGramatura() != null) {
                    ps.setDouble(3, item.getGramatura());
                } else {
                    ps.setNull(3, Types.DOUBLE);
                }
                if (item.getVezesPorDia() != null) {
                    ps.setInt(4, item.getVezesPorDia());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.addBatch();
                possuiItensValidos = true;
            }
            if (possuiItensValidos) {
                ps.executeBatch();
            }
        }
    }

    private void atualizarPlano(PlanoAlimentar plano, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL_ATUALIZAR_PLANO)) {
            ps.setLong(1, plano.getAnimal().getId());
            ps.setLong(2, plano.getId());
            ps.executeUpdate();
        }
    }

    private void limparItens(Long planoId, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL_EXCLUIR_ITENS)) {
            ps.setLong(1, planoId);
            ps.executeUpdate();
        }
    }

    private PlanoAlimentar mapearPlano(ResultSet rs) throws SQLException, PersistenciaException {
        Long id = rs.getLong("id");
        Long animalId = rs.getLong("animal_id");
        Animal animal = animalDAO.buscarPorId(animalId);
        Timestamp timestamp = rs.getTimestamp("data_criacao");
        LocalDateTime dataCriacao = timestamp != null ? timestamp.toLocalDateTime() : null;
        return new PlanoAlimentar(id, animal, dataCriacao);
    }

    private List<ItemPlanoAlimentar> buscarItens(Long planoId, Connection con) throws SQLException {
        List<ItemPlanoAlimentar> itens = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_ITENS)) {
            ps.setLong(1, planoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    itens.add(new ItemPlanoAlimentar(
                            rs.getLong("id"),
                            rs.getString("alimento"),
                            getNullableDouble(rs, "gramatura"),
                            getNullableInteger(rs, "vezes_por_dia")
                    ));
                }
            }
        }
        return itens;
    }

    private Double getNullableDouble(ResultSet rs, String coluna) throws SQLException {
        double valor = rs.getDouble(coluna);
        return rs.wasNull() ? null : valor;
    }

    private Integer getNullableInteger(ResultSet rs, String coluna) throws SQLException {
        int valor = rs.getInt(coluna);
        return rs.wasNull() ? null : valor;
    }

    private void validarPlano(PlanoAlimentar plano) throws PersistenciaException {
        if (plano == null || plano.getAnimal() == null || plano.getAnimal().getId() == null) {
            throw new PersistenciaException("Animal do plano alimentar é obrigatório.");
        }
    }

    private void rollbackSilencioso(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ignore) {
            }
        }
    }

    private void restaurarAutoCommit(Connection con, boolean valorAnterior) {
        if (con != null) {
            try {
                con.setAutoCommit(valorAnterior);
            } catch (SQLException ignore) {
            }
        }
    }
}
