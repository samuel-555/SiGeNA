package sigena.model.dao;

import sigena.model.domain.util.StatusOcorrencia;
import java.sql.*;

public class HistoricoOcorrenciaDAO {

    private final Connection con;

    public HistoricoOcorrenciaDAO(Connection con) {
        this.con = con;
    }

    public void registrar(Long ocorrenciaId,
                          StatusOcorrencia anterior,
                          StatusOcorrencia novo,
                          String cpf) {

        String sql = """
            INSERT INTO historico_status_ocorrencia
            (ocorrencia_id, status_anterior, status_novo, cpf_responsavel)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, ocorrenciaId);

            if (anterior != null) {
                ps.setString(2, anterior.name());
            } else {
                ps.setNull(2, Types.VARCHAR);
            }

            ps.setString(3, novo.name());
            ps.setString(4, cpf);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar histórico.", e);
        }
    }
}
