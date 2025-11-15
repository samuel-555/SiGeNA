package sigena.model.dao;

import java.sql.*;
import sigena.model.common.exception.DatabaseException;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Cargo;
import sigena.model.domain.Funcionario;
import sigena.model.domain.Usuario;
import sigena.model.util.ConexaoDB;

public class UsuarioDAO {

    private String normalizarCPF(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[\\.\\-]", "").trim();
    }

    public Usuario autenticar(String cpf, String senha) throws PersistenciaException {
        cpf = normalizarCPF(cpf);
        String sql = """
            SELECT id, cpf, senha, cargo 
            FROM usuarios 
            WHERE REPLACE(REPLACE(cpf,'.',''),'-','') = ? AND senha = ?;
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setCpf(rs.getString("cpf"));
                    u.setSenha(rs.getString("senha"));
                    u.setCargo(Cargo.valueOf(rs.getString("cargo")));
                    return u;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao autenticar usuário: " + e.getMessage());
        }
    }

    public boolean existeCPF(String cpf) throws PersistenciaException {
        cpf = normalizarCPF(cpf);
        String sql = "SELECT 1 FROM usuarios WHERE REPLACE(REPLACE(cpf,'.',''),'-','') = ?";

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

    public void criarUsuario(Funcionario f) throws DatabaseException {
        String sql = """
            INSERT INTO usuarios (cpf, senha, cargo, funcionario_id)
            SELECT ?, ?, ?, ?
            WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE cpf = ?);
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, f.getCpf());
            ps.setString(2, f.getSenha());
            ps.setString(3, f.getCargo().name());
            ps.setInt(4, f.getId());
            ps.setString(5, f.getCpf());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao criar usuário para funcionário: " + e.getMessage());
        }
    }

    public void atualizarSenhaOuCargo(Funcionario f) throws DatabaseException {
        String sql = "UPDATE usuarios SET senha=?, cargo=? WHERE funcionario_id=?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getSenha());
            ps.setString(2, f.getCargo().name());
            ps.setInt(3, f.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void deletarPorFuncionario(int funcionarioId) throws DatabaseException {
        String sql = "DELETE FROM usuarios WHERE funcionario_id=?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar usuário de funcionário: " + e.getMessage());
        }
    }

    public void sincronizarFuncionariosComUsuarios() throws DatabaseException {
        String sql = """
            INSERT INTO usuarios (cpf, senha, cargo, funcionario_id)
            SELECT f.cpf, f.senha, f.cargo, f.id
            FROM funcionarios f
            WHERE f.cpf NOT IN (SELECT u.cpf FROM usuarios u);
        """;
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int novos = ps.executeUpdate();
            if (novos > 0) {
                System.out.println("Sincronização concluída: " + novos + " usuários criados automaticamente.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao sincronizar usuários com funcionários: " + e.getMessage());
        }
    }
    public Usuario buscaPorId(int id) throws PersistenciaException {
        String sql = "SELECT id, cpf, senha, cargo FROM usuarios WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setCpf(rs.getString("cpf"));
                    u.setSenha(rs.getString("senha"));
                    u.setCargo(Cargo.valueOf(rs.getString("cargo")));
                    return u;
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar usuário por ID: " + e.getMessage());
        }
    }
}
