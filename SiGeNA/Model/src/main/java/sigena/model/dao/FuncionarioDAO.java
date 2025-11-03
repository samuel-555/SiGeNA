package sigena.model.dao;

import sigena.model.domain.Funcionario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    private Connection connection;

    public FuncionarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionario (nome, email, senha, cargo, data_cadastro) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, funcionario.getNome());
        stmt.setString(2, funcionario.getEmail());
        stmt.setString(3, funcionario.getSenha());
        stmt.setString(4, funcionario.getCargo());
        stmt.setTimestamp(5, Timestamp.valueOf(funcionario.getDataCadastro()));
        stmt.executeUpdate();
        stmt.close();
    }

    public List<Funcionario> listar() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionario ORDER BY id DESC";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Funcionario f = new Funcionario();
            f.setId(rs.getInt("id"));
            f.setNome(rs.getString("nome"));
            f.setEmail(rs.getString("email"));
            f.setSenha(rs.getString("senha"));
            f.setCargo(rs.getString("cargo"));
            f.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
            funcionarios.add(f);
        }

        rs.close();
        stmt.close();
        return funcionarios;
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        stmt.close();
    }
}
