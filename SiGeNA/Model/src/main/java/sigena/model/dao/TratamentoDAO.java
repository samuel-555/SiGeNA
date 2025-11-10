package sigena.model.dao;

import java.sql.*;
import sigena.model.domain.Animal;
import sigena.model.domain.Tratamento;
import sigena.model.domain.Usuario;
import sigena.model.util.ConexaoDB;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sigena.model.domain.util.StatusTratamento;
import sigena.model.domain.util.TipoTratamento;

public class TratamentoDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/tratamentos";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    public void cadastrar(Animal animal, Usuario usuario, Tratamento tratamento) {
        String sql = "INSERT INTO tratamentos(animal_id, vet_id, diagnostico, medicacao, frequencia, observacao, tipo, status, data_inicial, data_final) values (?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)";

        try {
            //Connection con = ConexaoDB.getConnection();
            Connection con = DriverManager.getConnection(URL, USUARIO, SENHA);
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
                ps.setTimestamp(9, java.sql.Timestamp.valueOf(tratamento.getDataFinal()));
            } else {
                ps.setNull(9, Types.TIMESTAMP);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tratamento> listar() {
        String sql = "SELECT * FROM tratamentos";
        List<Tratamento> tratamentos = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(URL, USUARIO, SENHA); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tratamentos.add(consultaToTratamento(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao exibir animais: " + e.getMessage());
        }

        return tratamentos;
    }

    private Tratamento consultaToTratamento(ResultSet rs) throws SQLException {
        
        Long animalId = rs.getLong("animal_id");
        AnimalDAO animalDAO = new AnimalDAO();
        Animal animal = animalDAO.buscarPorId(animalId);
        int vetId = rs.getInt("vet_id");
        Usuario vet = new Usuario();
        String diagnostico = rs.getString("diagnostico");
        String medicacao = rs.getString("medicacao");
        int frequencia = rs.getInt("frequencia");
        String observacao = rs.getString("observacao");
        String tipoStr = rs.getString("tipo");
        String statusStr = rs.getString("status");
        Timestamp dataFinal = rs.getTimestamp("data_final");
        TipoTratamento tipo = Enum.valueOf(TipoTratamento.class, tipoStr.toUpperCase());
        StatusTratamento status = Enum.valueOf(StatusTratamento.class, statusStr.toUpperCase());
        Tratamento aux = new Tratamento();
        
        
        return new Tratamento(animal, vetId, diagnostico, medicacao, frequencia, observacao, tipo, status, dataFinal);
    }

}
