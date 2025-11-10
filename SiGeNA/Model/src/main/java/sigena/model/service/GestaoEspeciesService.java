package sigena.model.service;
import java.util.List;
import sigena.model.dao.EspecieDAO;
import sigena.model.domain.Especie;
import sigena.model.common.exception.PersistenciaException;

public class GestaoEspeciesService {

    private EspecieDAO dao;

    public GestaoEspeciesService() {
        this.dao = new EspecieDAO();
    }

    public void inserir(Especie e) throws PersistenciaException {
        validarCampos(e);
        dao.inserir(e);
    }

    public void atualizar(Especie e) throws PersistenciaException {
        validarCampos(e);
        dao.atualizar(e);
    }

    public void excluir(int id) throws PersistenciaException {
        dao.excluir(id);
    }

    public Especie buscarPorId(int id) throws PersistenciaException {
        return dao.buscarPorId(id);
    }

    public List<Especie> listar() throws PersistenciaException {
        return dao.listar();
    }

    private void validarCampos(Especie e) throws PersistenciaException {
        if (e.getHabitat() == null || e.getHabitat().isBlank()
                || e.getAlimentacao() == null || e.getAlimentacao().isBlank()) {
            throw new PersistenciaException("Habitat e alimentação são obrigatórios!");
        }
    }
}