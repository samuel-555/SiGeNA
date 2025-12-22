package sigena.model.dao;

import sigena.model.domain.Funcionario;
import sigena.model.domain.util.Cargo;
import sigena.model.domain.util.Turno;
import sigena.model.domain.util.EstadoFuncionario;
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

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
                if (rs.next()) {
                    f.setId(rs.getInt(1));
                }
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

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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

    public void cancelar(int id) throws DatabaseException {
        String sql = "UPDATE funcionarios SET estado=? WHERE id=?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, EstadoFuncionario.CANCELADO.name());
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao cancelar funcionário: " + e.getMessage());
        }
    }

    public Funcionario buscarPorId(int id) throws DatabaseException {
        String sql = """
                     SELECT * FROM funcionarios 
                     WHERE id=? AND estado <> 'CANCELADO';""";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
        return listar(null);
    }

    public List<Funcionario> listar(String busca) throws DatabaseException {

        List<Funcionario> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM funcionarios ");
        sql.append("WHERE 1=1 ");
        sql.append("AND estado <> 'CANCELADO' ");

        boolean temBusca = busca != null && !busca.isBlank();
        if (temBusca) {
            sql.append("AND (nome LIKE ? OR cpf LIKE ?) ");
        }

        sql.append("ORDER BY nome");

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;

            if (temBusca) {
                ps.setString(index++, "%" + busca + "%");
                ps.setString(index++, "%" + busca + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapFuncionario(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar funcionários: " + e.getMessage());
        }

        return lista;
    }

    public List<Funcionario> buscarComFiltro(
            String nome,
            String cargo,
            String turno,
            String estado
    ) throws DatabaseException {

        List<Funcionario> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT * FROM funcionarios
        WHERE estado <> 'CANCELADO'
    """);

        List<Object> params = new ArrayList<>();

        if (nome != null && !nome.isBlank()) {
            sql.append(" AND nome LIKE ?");
            params.add("%" + nome + "%");
        }

        if (cargo != null && !cargo.isBlank()) {
            sql.append(" AND cargo = ?");
            params.add(cargo);
        }

        if (turno != null && !turno.isBlank()) {
            sql.append(" AND turno = ?");
            params.add(turno);
        }

        if (estado != null && !estado.isBlank()) {
            sql.append(" AND estado = ?");
            params.add(estado);
        }

        sql.append(" ORDER BY nome");

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapFuncionario(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao pesquisar funcionários: " + e.getMessage());
        }

        return lista;
    }
    
    public int contarAtivosPorAreaTurnoExcetoId(
        int id,
        String areaAtuacao,
        Turno turno
) throws DatabaseException {

    String sql = """
        SELECT COUNT(*) 
        FROM funcionarios
        WHERE estado = 'ATIVO'
          AND area_atuacao = ?
          AND turno = ?
          AND id <> ?
    """;

    try (Connection con = ConexaoDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, areaAtuacao);
        ps.setString(2, turno.name());
        ps.setInt(3, id);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

    } catch (SQLException e) {
        throw new DatabaseException(
            "Erro ao contar funcionários ativos por setor e turno: " + e.getMessage()
        );
    }

    return 0;
}


    public List<Funcionario> pesquisar(
            String nome,
            String cpf,
            Cargo cargo,
            Turno turno,
            EstadoFuncionario estado
    ) throws DatabaseException {

        List<Funcionario> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT * FROM funcionarios
        WHERE estado <> 'CANCELADO'
    """
        );

        if (nome != null && !nome.isEmpty()) {
            sql.append(" AND nome LIKE ?");
        }
        if (cpf != null && !cpf.isEmpty()) {
            sql.append(" AND cpf = ?");
        }
        if (cargo != null) {
            sql.append(" AND cargo = ?");
        }
        if (turno != null) {
            sql.append(" AND turno = ?");
        }
        if (estado != null) {
            sql.append(" AND estado = ?");
        }

        sql.append(" ORDER BY nome");

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int i = 1;

            if (nome != null && !nome.isEmpty()) {
                ps.setString(i++, "%" + nome + "%");
            }
            if (cpf != null && !cpf.isEmpty()) {
                ps.setString(i++, cpf);
            }
            if (cargo != null) {
                ps.setString(i++, cargo.name());
            }
            if (turno != null) {
                ps.setString(i++, turno.name());
            }
            if (estado != null) {
                ps.setString(i++, estado.name());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapFuncionario(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao pesquisar funcionários: " + e.getMessage());
        }

        return lista;
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
