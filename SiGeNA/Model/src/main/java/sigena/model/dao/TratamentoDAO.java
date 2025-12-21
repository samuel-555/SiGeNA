package sigena.model.dao;

import java.sql.*;
import java.time.LocalDate;
import sigena.model.domain.Animal;
import sigena.model.domain.Tratamento;
import sigena.model.domain.Usuario;
import sigena.model.util.ConexaoDB;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import sigena.model.common.exception.DatabaseException;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Funcionario;
import sigena.model.domain.util.StatusTratamento;
import sigena.model.domain.util.TipoTratamento;

public class TratamentoDAO {

    public void cadastrar(Animal animal, Usuario usuario, Tratamento tratamento) throws PersistenciaException {
        String sql = "INSERT INTO tratamento(animal_id, vet_id, diagnostico, medicacao, frequencia, observacao, tipo, status, data_inicio, data_final, horario) values (?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)";

        try {
            Connection con = ConexaoDB.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, animal.getId());
            ps.setInt(2, usuario.getId());
            ps.setString(3, tratamento.getDiagnostico());
            ps.setString(4, tratamento.getMedicacao());
            ps.setInt(5, tratamento.getFrequencia());
            ps.setString(6, tratamento.getObservacao());
            ps.setString(7, tratamento.getTipoTratamento());
            ps.setString(8, tratamento.getStatusTratamento());
            if (tratamento.getDataFinal() != null) {
                ps.setDate(9, java.sql.Date.valueOf(tratamento.getDataFinal()));
            } else {
                ps.setNull(9, Types.DATE);
            }

            if (tratamento.getHorario() != null) {
                ps.setTime(10, java.sql.Time.valueOf(tratamento.getHorario()));
            } else {
                ps.setNull(10, Types.TIME);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tratamento> listar(String busca, String status, String tipo) throws PersistenciaException, DatabaseException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.* FROM tratamento t ");
        sql.append("INNER JOIN animais a ON t.animal_id = a.id ");
        sql.append("WHERE 1=1 ");
        boolean temBusca = busca != null && !busca.isEmpty();
        if (temBusca) {
            sql.append(" AND (a.nome LIKE ? OR t.animal_id LIKE ?)");
        }
        boolean temStatus = status != null && !status.isEmpty();
        if (temStatus) {
            sql.append(" AND status = ?");
        }
        boolean temTipo = tipo != null && !tipo.isEmpty();
        if (temTipo) {
            sql.append(" AND tipo = ?");
        }
        sql.append(" ORDER BY data_inicio DESC");
        List<Tratamento> tratamentos = new ArrayList<>();
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int index = 1;

            if (temBusca) {
                ps.setString(index++, "%" + busca + "%");
                ps.setString(index++, "%" + busca + "%");
            }

            if (temStatus) {
                ps.setString(index++, status);
            }

            if (temTipo) {
                ps.setString(index++, tipo);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tratamentos.add(consultaToTratamento(rs));
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar tratamentos: " + e.getMessage());
        }

        return tratamentos;
    }

    private Tratamento consultaToTratamento(ResultSet rs) throws SQLException, PersistenciaException, DatabaseException {

        Long animalId = rs.getLong("animal_id");
        AnimalDAO animalDAO = new AnimalDAO();
        Animal animal = animalDAO.buscarPorId(animalId);
        int vetId = rs.getInt("vet_id");
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        Funcionario funcionario = funcionarioDAO.buscarPorId(vetId);
        Usuario vet = null;
        if (funcionario != null) {
            vet = new Usuario();
            vet.setId(funcionario.getId());
            vet.setCpf(funcionario.getCpf());
            vet.setCargo(funcionario.getCargo());
        }
        String diagnostico = rs.getString("diagnostico");
        String medicacao = rs.getString("medicacao");
        int frequencia = rs.getInt("frequencia");
        String observacao = rs.getString("observacao");
        String tipoStr = rs.getString("tipo");
        String statusStr = rs.getString("status");
        LocalDate dataFinal = rs.getDate("data_final").toLocalDate();
        LocalTime horario = rs.getTime("horario") != null
                ? rs.getTime("horario").toLocalTime()
                : null;
        int id = rs.getInt("id");
        TipoTratamento tipo = Enum.valueOf(TipoTratamento.class, tipoStr.toUpperCase());
        StatusTratamento status = Enum.valueOf(StatusTratamento.class, statusStr.toUpperCase());

        Tratamento t = new Tratamento(animal, vet, diagnostico, medicacao, frequencia, observacao, tipo, status, dataFinal, horario);
        t.setId(id);
        return t;
    }

    public void editar(Tratamento tratamento) throws PersistenciaException {
        String sql = "UPDATE tratamento SET animal_id = ?, diagnostico = ?, medicacao = ?, frequencia = ?, observacao = ?, tipo = ?, status = ?, data_final = ?, horario = ? WHERE id = ?";
        try {
            Connection con = ConexaoDB.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, tratamento.getAnimal().getId());
            ps.setString(2, tratamento.getDiagnostico());
            ps.setString(3, tratamento.getMedicacao());
            ps.setInt(4, tratamento.getFrequencia());
            ps.setString(5, tratamento.getObservacao());
            ps.setString(6, tratamento.getTipoTratamento());
            ps.setString(7, tratamento.getStatusTratamento());
            if (tratamento.getDataFinal() != null) {
                ps.setDate(8, java.sql.Date.valueOf(tratamento.getDataFinal()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            if (tratamento.getHorario() != null) {
                ps.setTime(9, java.sql.Time.valueOf(tratamento.getHorario()));
            } else {
                ps.setNull(9, Types.TIME);
            }
            ps.setInt(10, tratamento.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Tratamento buscarPorId(int id) throws PersistenciaException, DatabaseException {
        String sql = "SELECT * FROM tratamento WHERE id = ?";
        try {
            Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Tratamento t = new Tratamento();
                t = consultaToTratamento(rs);

                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void cancelar(int id) throws PersistenciaException, DatabaseException {
        String sql = "UPDATE tratamento SET status = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            Tratamento t = buscarPorId(id);
            t.setStatusTratamento(StatusTratamento.CANCELADO);
            ps.setString(1, t.getStatusTratamento());
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao cancelar tratamento: " + e.getMessage());
        }

    }
}
