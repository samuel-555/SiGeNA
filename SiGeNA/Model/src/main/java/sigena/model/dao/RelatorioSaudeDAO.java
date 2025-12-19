package sigena.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Animal;
import sigena.model.domain.RelatorioSaude;
import sigena.model.util.ConexaoDB;

public class RelatorioSaudeDAO {

    private final AnimalDAO animalDAO = new AnimalDAO();

    public RelatorioSaude cadastrar(RelatorioSaude relatorio) throws PersistenciaException {
        String sql = """
            INSERT INTO relatorios_saude (animal_id, data_relatorio, peso, status, observacoes, data_criacao)
            VALUES (?, ?, ?, ?, ?, NOW())
            """;

        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, relatorio.getAnimal().getId());
            stmt.setDate(2, Date.valueOf(relatorio.getDataRelatorio()));
            if (relatorio.getPeso() == null) {
                stmt.setNull(3, Types.DOUBLE);
            } else {
                stmt.setDouble(3, relatorio.getPeso());
            }
            stmt.setString(4, relatorio.getStatus());
            if (relatorio.getObservacoes() == null) {
                stmt.setNull(5, Types.LONGVARCHAR);
            } else {
                stmt.setString(5, relatorio.getObservacoes());
            }
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    relatorio.setId(rs.getLong(1));
                }
            }

            return relatorio;
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel registrar o relatorio: " + e.getMessage());
        }
    }

    public List<RelatorioSaude> listarPorAnimal(Long animalId) throws PersistenciaException {
        return listarPorFiltros(animalId, null);
    }

    public List<RelatorioSaude> listarTodos() throws PersistenciaException {
        return listarPorFiltros(null, null);
    }

    public List<RelatorioSaude> listarPorFiltros(Long animalId, String status) throws PersistenciaException {
        StringBuilder sql = new StringBuilder("""
            SELECT id, animal_id, data_relatorio, peso, status, observacoes
            FROM relatorios_saude
            WHERE 1 = 1
            """);
        List<Object> parametros = new ArrayList<>();

        if (animalId != null) {
            sql.append(" AND animal_id = ?");
            parametros.add(animalId);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND UPPER(status) = ?");
            parametros.add(status.toUpperCase());
        }
        sql.append(" ORDER BY data_relatorio DESC, id DESC");

        List<RelatorioSaude> relatorios = new ArrayList<>();
        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                Object valor = parametros.get(i);
                int indice = i + 1;
                if (valor instanceof Long) {
                    stmt.setLong(indice, (Long) valor);
                } else {
                    stmt.setString(indice, valor.toString());
                }
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    relatorios.add(toRelatorio(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel consultar os relatorios: " + e.getMessage());
        }

        return relatorios;
    }

    public RelatorioSaude buscarPorId(Long id) throws PersistenciaException {
        String sql = """
            SELECT id, animal_id, data_relatorio, peso, status, observacoes
            FROM relatorios_saude
            WHERE id = ?
            """;

        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return toRelatorio(rs);
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel buscar relatorio: " + e.getMessage());
        }

        return null;
    }

    public void excluir(Long id) throws PersistenciaException {
        String sql = "DELETE FROM relatorios_saude WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel excluir o relatorio: " + e.getMessage());
        }
    }

    public void atualizar(RelatorioSaude relatorio) throws PersistenciaException {
        String sql = """
            UPDATE relatorios_saude
            SET animal_id = ?, data_relatorio = ?, peso = ?, status = ?, observacoes = ?
            WHERE id = ?
            """;

        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, relatorio.getAnimal().getId());
            stmt.setDate(2, Date.valueOf(relatorio.getDataRelatorio()));
            if (relatorio.getPeso() == null) {
                stmt.setNull(3, Types.DOUBLE);
            } else {
                stmt.setDouble(3, relatorio.getPeso());
            }
            stmt.setString(4, relatorio.getStatus());
            if (relatorio.getObservacoes() == null) {
                stmt.setNull(5, Types.LONGVARCHAR);
            } else {
                stmt.setString(5, relatorio.getObservacoes());
            }
            stmt.setLong(6, relatorio.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel atualizar o relatorio: " + e.getMessage());
        }
    }

    public void acrescentarObservacao(Long relatorioId, String novaObservacao) throws PersistenciaException {
        RelatorioSaude relatorio = buscarPorId(relatorioId);
        if (relatorio == null) {
            throw new PersistenciaException("Relatorio nao encontrado.");
        }

        StringBuilder textoFinal = new StringBuilder();
        if (relatorio.getObservacoes() != null && !relatorio.getObservacoes().isBlank()) {
            textoFinal.append(relatorio.getObservacoes().trim());
            textoFinal.append(System.lineSeparator());
        }
        textoFinal.append(novaObservacao);

        String sql = "UPDATE relatorios_saude SET observacoes = ? WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, textoFinal.toString());
            stmt.setLong(2, relatorioId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Nao foi possivel atualizar observacoes: " + e.getMessage());
        }
    }

    private RelatorioSaude toRelatorio(ResultSet rs) throws SQLException, PersistenciaException {
        Long relatorioId = rs.getLong("id");
        Long animalId = rs.getLong("animal_id");
        LocalDate dataRelatorio = rs.getDate("data_relatorio").toLocalDate();
        Double peso = rs.getObject("peso") != null ? rs.getDouble("peso") : null;
        String status = rs.getString("status");
        String observacoes = rs.getString("observacoes");
        Animal animal = animalDAO.buscarPorId(animalId);

        return new RelatorioSaude(relatorioId, animal, dataRelatorio, peso, status, observacoes);
    }
}
