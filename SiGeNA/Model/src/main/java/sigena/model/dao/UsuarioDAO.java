package sigena.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sigena.model.util.ConexaoDB;
import sigena.model.common.exception.PersistenciaException;

public class UsuarioDAO {

    private String normalizarCPF(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[\\.\\-]", "").trim();
    }

    public boolean autenticar(String cpf, String senha) throws PersistenciaException {
        cpf = normalizarCPF(cpf);
        senha = senha.trim();
        String sql = """
            SELECT * FROM usuarios 
            WHERE REPLACE(REPLACE(cpf,'.',''),'-','') = ? AND senha = ?
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao autenticar usuário: " + e.getMessage());
        }
    }

    public String cadastrar(String cpf, String senha) throws PersistenciaException {
        cpf = normalizarCPF(cpf);
        senha = senha.trim();

        if (existeCPF(cpf)) {
            return "CPF já cadastrado! Não é permitido mais de um cadastro com o mesmo CPF.";
        }

        String sql = "INSERT INTO usuarios (cpf, senha) VALUES (?, ?)";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ps.setString(2, senha);
            ps.executeUpdate();
            return "Usuário cadastrado com sucesso!";

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public boolean existeCPF(String cpf) throws PersistenciaException {
        cpf = normalizarCPF(cpf);
        String sql = "SELECT * FROM usuarios WHERE REPLACE(REPLACE(cpf,'.',''),'-','') = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao verificar CPF: " + e.getMessage());
        }
    }
}