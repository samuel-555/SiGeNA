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

    private static final String STATUS_ATIVO = "ATIVO";
    private static final String STATUS_CANCELADO = "CANCELADO";

    private static final String SQL_INSERIR_PLANO = """
            INSERT INTO planos_alimentares (animal_id, status, data_criacao)
            VALUES (?, ?, NOW())
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
            SELECT id, animal_id, status, data_criacao
            FROM planos_alimentares
            WHERE status IS NULL OR UPPER(status) <> 'CANCELADO'
            ORDER BY id DESC
            """;

    private static final String SQL_BUSCAR_PLANO = """
            SELECT id, animal_id, status, data_criacao
            FROM planos_alimentares
            WHERE id = ? AND (status IS NULL OR UPPER(status) <> 'CANCELADO')
            """;

    private static final String SQL_BUSCAR_ITENS = """
            SELECT id, alimento, gramatura, vezes_por_dia
            FROM itens_plano_alimentar
            WHERE plano_id = ?
            ORDER BY id
            """;

    private static final String SQL_EXCLUIR_PLANO = """
            UPDATE planos_alimentares SET status = 'CANCELADO' WHERE id = ?
            """;

    private static final String SQL_BUSCAR_STATUS = """
            SELECT status FROM planos_alimentares WHERE id = ?
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
                String statusAtual = buscarStatus(con, plano.getId());
                if (statusAtual == null) {
                    throw new PersistenciaException("Plano alimentar nao encontrado.");
                }
                if (STATUS_CANCELADO.equalsIgnoreCase(statusAtual)) {
                    throw new PersistenciaException("Plano alimentar cancelado nao pode ser editado.");
                }
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
            String statusAtual = buscarStatus(con, id);
            if (statusAtual == null) {
                throw new PersistenciaException("Plano alimentar nao encontrado.");
            }
            if (STATUS_CANCELADO.equalsIgnoreCase(statusAtual)) {
                throw new PersistenciaException("Plano alimentar ja cancelado.");
            }
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao excluir plano alimentar: " + e.getMessage());
        }
    }

    public List<PlanoAlimentar> listar() throws PersistenciaException {
        return listar(null, null);
    }

    public List<PlanoAlimentar> listar(Long animalId, String ingrediente) throws PersistenciaException {
        List<PlanoAlimentar> planos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT id, animal_id, status, data_criacao
                FROM planos_alimentares
                WHERE status IS NULL OR UPPER(status) <> 'CANCELADO'
                """);
        List<Object> params = new ArrayList<>();

        if (animalId != null) {
            sql.append(" AND animal_id = ?");
            params.add(animalId);
        }
        if (ingrediente != null && !ingrediente.isBlank()) {
            sql.append("""
                    AND EXISTS (
                        SELECT 1
                        FROM itens_plano_alimentar
                        WHERE itens_plano_alimentar.plano_id = planos_alimentares.id
                          AND LOWER(itens_plano_alimentar.alimento) LIKE ?
                    )
                    """);
            params.add("%" + ingrediente.trim().toLowerCase() + "%");
        }

        sql.append(" ORDER BY id DESC");

        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object valor = params.get(i);
                int idx = i + 1;
                if (valor instanceof Long) {
                    ps.setLong(idx, (Long) valor);
                } else {
                    ps.setString(idx, valor.toString());
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PlanoAlimentar plano = mapearPlano(rs);
                plano.setItens(buscarItens(plano.getId(), con));
                planos.add(plano);
            }
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
            ps.setString(2, STATUS_ATIVO);
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
        String status = rs.getString("status");
        Timestamp timestamp = rs.getTimestamp("data_criacao");
        LocalDateTime dataCriacao = timestamp != null ? timestamp.toLocalDateTime() : null;
        PlanoAlimentar plano = new PlanoAlimentar(id, animal, dataCriacao);
        plano.setStatus(status);
        return plano;
    }

    private String buscarStatus(Connection con, Long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_STATUS)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                }
            }
        }
        return null;
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
