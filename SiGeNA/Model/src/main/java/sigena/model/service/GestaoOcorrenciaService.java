package sigena.model.service;

import sigena.model.dao.OcorrenciaDAO;
import sigena.model.domain.Ocorrencia;
import java.util.List;

public class GestaoOcorrenciaService {

    private final OcorrenciaDAO dao;

    public GestaoOcorrenciaService(OcorrenciaDAO dao) {
        this.dao = dao;
    }

    public void criar(Ocorrencia o) {
        dao.criar(o);
    }

    public List<Ocorrencia> listar() {
        return dao.listar();
    }

    public List<Ocorrencia> buscarComFiltro(String tipo, String status, String texto) {
        return dao.buscarComFiltro(tipo, status, texto);
    }

    public Ocorrencia buscar(Long id) {
        return dao.buscarPorId(id);
    }

    public void atualizar(Ocorrencia o) {
        dao.atualizar(o);
    }

    public void cancelar(Long id) {
        dao.cancelar(id);
    }

}
