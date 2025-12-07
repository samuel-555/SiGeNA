package sigena.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Fornecedor;
import sigena.model.util.ConexaoDB;

public class FornecedorDAO {

    public void cadastrar(Fornecedor fornecedor) throws PersistenciaException {
        String sql = "INSERT INTO fornecedores (nome, telefone, email, endereco, tipo, descricao, data_de_insercao) "
                   + "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setPreparedStatementInsert(stmt, fornecedor);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    fornecedor.setId(id);
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível cadastrar fornecedor: " + e.getMessage());
        }
    }

    public List<Fornecedor> listar(String busca, String filtro) throws PersistenciaException {
        String sql = "SELECT * FROM fornecedores "
                + "WHERE (id LIKE ? OR nome LIKE ?) ";
        
        if(filtro != null && !filtro.isEmpty())
            sql += "AND STRCMP(tipo, ?) = 0";
        
        sql += " ORDER BY data_de_insercao ASC;";

        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + busca + "%");
            stmt.setString(2, "%" + busca + "%");
            
            if(filtro != null && !filtro.isEmpty())
                stmt.setString(3, filtro);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                fornecedores.add(consultaToFornecedor(rs));

        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível listar fornecedores: " + e.getMessage());
        }

        return fornecedores;
    }

    public Fornecedor buscarPorId(Long id) throws PersistenciaException {
        String sql = "SELECT * FROM fornecedores WHERE id = ?";

        Fornecedor fornecedor = null;

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                fornecedor = consultaToFornecedor(rs);
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível buscar fornecedor: " + e.getMessage());
        }

        return fornecedor;
    }

    public void excluir(Long id) throws PersistenciaException {
        String sql = "DELETE FROM fornecedores WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível excluir fornecedor: " + e.getMessage());
        }
    }

    public void editar(Fornecedor fornecedor) throws PersistenciaException {
        String sql = "UPDATE fornecedores "
                   + "SET nome = ?, telefone = ?, email = ?, endereco = ?, tipo = ?, descricao = ? "
                   + "WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            setPreparedStatementUpdate(stmt, fornecedor);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Não foi possível editar fornecedor: " + e.getMessage());
        }
    }

    private Fornecedor consultaToFornecedor(ResultSet rs) throws SQLException, PersistenciaException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        String telefone = rs.getString("telefone");
        String email = rs.getString("email");
        String endereco = rs.getString("endereco");
        String tipo = rs.getString("tipo");
        String descricao = rs.getString("descricao");

        return new Fornecedor(id, nome, telefone, email, endereco, tipo, descricao);
    }

    private void setPreparedStatementInsert(PreparedStatement stmt, Fornecedor fornecedor) throws SQLException {
        stmt.setString(1, fornecedor.getNome());
        stmt.setString(2, fornecedor.getTelefone());
        stmt.setString(3, fornecedor.getEmail());
        stmt.setString(4, fornecedor.getEndereco());
        stmt.setString(5, fornecedor.getTipo());
        stmt.setString(6, fornecedor.getDescricao());
    }

    private void setPreparedStatementUpdate(PreparedStatement stmt, Fornecedor fornecedor) throws SQLException {
        setPreparedStatementInsert(stmt, fornecedor);
        stmt.setLong(7, fornecedor.getId());
    }
}

