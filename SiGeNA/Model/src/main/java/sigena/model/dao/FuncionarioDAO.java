package sigena.model.dao;

import sigena.model.domain.Funcionario;
import sigena.model.domain.Cargo;
import sigena.model.domain.Turno;
import sigena.model.domain.EstadoFuncionario;
import sigena.model.util.ConexaoDB;
import sigena.model.common.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public void salvar(Funcionario f) throws DatabaseException {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            if (usuarioDAO.existeCPF(f.getCpf())) {
                throw new DatabaseException("Já existe um usuário com este CPF!");
            }
        } catch (Exception e) {
            throw new DatabaseException("Erro ao validar CPF: " + e.getMessage());
        }

        String sql = """
            INSERT INTO funcionarios 
            (nome, cpf, senha, cargo, area_atuacao, turno, estado, observacoes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setString(3, f.getSenha());
            ps.setString(4, f.getCargo().name());
            ps.setString(5, f.getAreaAtuacao());
            ps.setString(6, f.getTurno().name());
            ps.setString(7, f.getEstado().name());
            ps.setString(8, f.getObservacoes());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) f.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar funcionário: " + e.getMessage());
        }
    }

    public void atualizar(Funcionario f) throws DatabaseException {
        String sql = """
            UPDATE funcionarios SET 
                nome=?, cpf=?, senha=?, cargo=?, area_atuacao=?, turno=?, estado=?, observacoes=?
            WHERE id=?;
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setString(3, f.getSenha());
            ps.setString(4, f.getCargo().name());
            ps.setString(5, f.getAreaAtuacao());
            ps.setString(6, f.getTurno().name());
            ps.setString(7, f.getEstado().name());
            ps.setString(8, f.getObservacoes());
            ps.setInt(9, f.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar funcionário: " + e.getMessage());
        }
    }

    public void deletar(int id) throws DatabaseException {
        String sql = "DELETE FROM funcionarios WHERE id=?;";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar funcionário: " + e.getMessage());
        }
    }

    public Funcionario buscarPorId(int id) throws DatabaseException {
        String sql = "SELECT * FROM funcionarios WHERE id=?;";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFuncionario(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar funcionário: " + e.getMessage());
        }
        return null;
    }

    public List<Funcionario> listar() throws DatabaseException {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios ORDER BY nome;";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapFuncionario(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar funcionários: " + e.getMessage());
        }

        return lista;
    }

    public int contarPorSetorETurnoExceto(String setor, String turno, int idExcluir) throws DatabaseException {
        String sql = """
            SELECT COUNT(*) FROM funcionarios 
            WHERE area_atuacao=? AND turno=? AND id<>?;
        """;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, setor);
            ps.setString(2, turno);
            ps.setInt(3, idExcluir);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar funcionários: " + e.getMessage());
        }
        return 0;
    }

    private Funcionario mapFuncionario(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setId(rs.getInt("id"));
        f.setNome(rs.getString("nome"));
        f.setCpf(rs.getString("cpf"));
        f.setSenha(rs.getString("senha"));
        f.setCargo(Cargo.valueOf(rs.getString("cargo")));
        f.setAreaAtuacao(rs.getString("area_atuacao"));
        f.setTurno(Turno.valueOf(rs.getString("turno")));
        f.setEstado(EstadoFuncionario.valueOf(rs.getString("estado")));
        f.setObservacoes(rs.getString("observacoes"));
        return f;
    }
}
