package sigena.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Fornecedor;
import sigena.model.domain.Produto;
import sigena.model.domain.util.TipoProduto;
import sigena.model.util.ConexaoDB;

public class ProdutoDAO {

    public void cadastrar(Produto produto, Fornecedor fornecedor) throws PersistenciaException {
        String sql = "INSERT INTO produtos(fornecedor_id, quantidade, nome, tipo, lote, validade, disponivel) values (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection con = ConexaoDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, fornecedor.getId());
            ps.setInt(2, produto.getQuantidade());
            ps.setString(3, produto.getNome());
            ps.setString(4, produto.getTipo().name());

            if (produto.getLote() != null) {
                ps.setDate(5, java.sql.Date.valueOf(produto.getLote()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            if (produto.getValidade() != null) {
                ps.setDate(6, java.sql.Date.valueOf(produto.getValidade()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setBoolean(7, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Produto> listar() throws PersistenciaException {
        String sql = "SELECT * FROM produtos WHERE disponivel= true";
        List<Produto> produtos = new ArrayList<>();

        try (
                Connection conn = ConexaoDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                Long fId = rs.getLong("fornecedor_id");
                FornecedorDAO fDAO = new FornecedorDAO();
                Fornecedor fornecedor = fDAO.buscarPorId(fId);
                produto.setFornecedor(fornecedor);
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setDisponivel(rs.getBoolean("disponivel"));

                Date validade = rs.getDate("validade");
                if (validade != null) {
                    produto.setValidade(validade.toLocalDate());
                }

                Date lote = rs.getDate("lote");
                if (lote != null) {
                    produto.setLote(lote.toLocalDate());
                }

                String tipoStr = rs.getString("tipo");
                produto.setTipo(TipoProduto.valueOf(rs.getString("tipo")));

                produtos.add(produto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public Produto buscar(Long id) throws PersistenciaException {
        String sql = "SELECT * FROM produtos WHERE id = ?";

        try (Connection conn = ConexaoDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    Produto produto = new Produto();
                    Long fId = rs.getLong("id_fornecedor");
                    FornecedorDAO fDAO = new FornecedorDAO();
                    Fornecedor fornecedor = fDAO.buscarPorId(fId);
                    produto.setFornecedor(fornecedor);
                    produto.setId(rs.getLong("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    produto.setDisponivel(rs.getBoolean("disponivel"));

                    Date validade = rs.getDate("validade");
                    if (validade != null) {
                        produto.setValidade(validade.toLocalDate());
                    }

                    Date lote = rs.getDate("lote");
                    if (lote != null) {
                        produto.setLote(lote.toLocalDate());
                    }

                    String tipoStr = rs.getString("tipo");
                    produto.setTipo(TipoProduto.valueOf(rs.getString("tipo")));
                    return produto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void excluir(Long id) throws PersistenciaException {
        String sql = "UPDATE produtos SET disponivel = false WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao excluir produto: " + e.getMessage());
        }
    }

    public void alterar(Produto produto) throws PersistenciaException {
        String sql = "UPDATE produtos SET nome = ?, fornecedor_id = ?, quantidade = ?, tipo = ?, lote = ?, validade = ?, disponivel = ? WHERE id = ?";

        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, produto.getNome());
            ps.setLong(2, produto.getFornecedor().getId());
            ps.setInt(3, produto.getQuantidade());
            ps.setString(4, produto.getTipo().name());

            if (produto.getLote() != null) {
                ps.setDate(5, Date.valueOf(produto.getLote()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            if (produto.getValidade() != null) {
                ps.setDate(6, Date.valueOf(produto.getValidade()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setBoolean(7, produto.getDisponivel());
            ps.setLong(8, produto.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar produto: " + e.getMessage());
        }
    }
}
