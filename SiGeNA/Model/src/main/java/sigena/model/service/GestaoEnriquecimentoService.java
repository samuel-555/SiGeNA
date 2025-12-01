package sigena.model.service;

import sigena.model.dao.EnriquecimentoDAO;
import sigena.model.domain.Enriquecimento;

import java.sql.SQLException;
import java.util.List;

public class GestaoEnriquecimentoService {

    private final EnriquecimentoDAO dao = new EnriquecimentoDAO();

    public int criarEnriquecimento(Enriquecimento e, List<String> habitats) throws SQLException, IllegalArgumentException {
        if (habitats == null || habitats.isEmpty()) {
            throw new IllegalArgumentException("Todo enriquecimento deve estar vinculado a pelo menos um habitat.");
        }
        return dao.insert(e, habitats);
    }

    public List<Enriquecimento> listarTodos() throws SQLException {
        return dao.findAll();
    }

    public Enriquecimento buscarPorId(int id) throws SQLException {
        return dao.findById(id);
    }

    public void atualizarEnriquecimento(Enriquecimento e, List<String> habitats)
            throws SQLException, IllegalArgumentException {

        if (habitats == null || habitats.isEmpty()) {
            throw new IllegalArgumentException("Todo enriquecimento deve estar vinculado a pelo menos um habitat.");
        }

        dao.update(e, habitats);
    }

    public List<String> listarHabitatsDisponiveis() throws SQLException {
        return dao.findAllHabitats();
    }

    public void remover(int id) throws SQLException {
        dao.delete(id);
    }
}
