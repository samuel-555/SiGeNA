package sigena.model.dao;

import java.sql.*;
import java.util.*;
import sigena.model.domain.Especie;
import sigena.model.util.ConexaoDB;
import sigena.model.common.exception.PersistenciaException;

public class EspecieDAO {

    private void validarCamposObrigatorios(Especie e) throws PersistenciaException {
        if (e.getHabitat() == null || e.getHabitat().isBlank()
                || e.getAlimentacao() == null || e.getAlimentacao().isBlank()) {
            throw new PersistenciaException("Habitat e alimentação são obrigatórios!");
        }
    }

    public void inserir(Especie e) throws PersistenciaException {
        validarCamposObrigatorios(e);
        String sql = "INSERT INTO especie (nome, classe, habitat, alimentacao, predador, observacoes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getClasse());
            ps.setString(3, e.getHabitat());
            ps.setString(4, e.getAlimentacao());
            ps.setBoolean(5, e.isPredador());
            ps.setString(6, e.getObservacoes());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao cadastrar espécie: " + ex.getMessage());
        }
    }

    public List<Especie> listar() throws PersistenciaException {
        List<Especie> lista = new ArrayList<>();
        String sql = "SELECT * FROM especie ORDER BY nome";
        try (Connection con = ConexaoDB.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Especie e = new Especie();
                e.setId(rs.getInt("id"));
                e.setNome(rs.getString("nome"));
                e.setClasse(rs.getString("classe"));
                e.setHabitat(rs.getString("habitat"));
                e.setAlimentacao(rs.getString("alimentacao"));
                e.setPredador(rs.getBoolean("predador"));
                e.setObservacoes(rs.getString("observacoes"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao listar espécies: " + ex.getMessage());
        }
        return lista;
    }

    public void excluir(int id) throws PersistenciaException {
        if (existeAnimalVinculado(id)) {
            throw new PersistenciaException("Não é possível excluir: existem animais vinculados a esta espécie.");
        }
        String sql = "DELETE FROM especie WHERE id = ?";
        try (Connection con = ConexaoDB.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao excluir espécie: " + ex.getMessage());
        }
    }

    public Especie buscarPorId(int id) throws PersistenciaException {
        String sql = "SELECT * FROM especie WHERE id = ?";
        Especie e = null;
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    e = new Especie();
                    e.setId(rs.getInt("id"));
                    e.setNome(rs.getString("nome"));
                    e.setClasse(rs.getString("classe"));
                    e.setHabitat(rs.getString("habitat"));
                    e.setAlimentacao(rs.getString("alimentacao"));
                    e.setPredador(rs.getBoolean("predador"));
                    e.setObservacoes(rs.getString("observacoes"));
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao buscar espécie: " + ex.getMessage());
        }
        return e;
    }

    public void atualizar(Especie e) throws PersistenciaException {
        validarCamposObrigatorios(e);
        String sql = "UPDATE especie SET nome=?, classe=?, habitat=?, alimentacao=?, predador=?, observacoes=? WHERE id=?";
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getClasse());
            ps.setString(3, e.getHabitat());
            ps.setString(4, e.getAlimentacao());
            ps.setBoolean(5, e.isPredador());
            ps.setString(6, e.getObservacoes());
            ps.setInt(7, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao editar espécie: " + ex.getMessage());
        }
    }

    private boolean existeAnimalVinculado(int idEspecie) throws PersistenciaException {
        String sql = """
            SELECT COUNT(*) FROM animais
            WHERE id_especie = (
                SELECT nome FROM especie WHERE id = ?
            )
        """;
        try (Connection con = ConexaoDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEspecie);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao verificar vínculo de animais: " + ex.getMessage());
        }

        return false;
    }
}
