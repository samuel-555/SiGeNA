package sigena.model.service;

import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.PlanoAlimentarDAO;
import sigena.model.domain.PlanoAlimentar;
import sigena.model.domain.util.TipoHistorico;

public class GestaoPlanoAlimentarService {

    private final PlanoAlimentarDAO dao;
    private final GestaoHistoricoService historicoService;

    public GestaoPlanoAlimentarService() {
        dao = new PlanoAlimentarDAO();
        historicoService = new GestaoHistoricoService();
    }

    public void cadastrar(PlanoAlimentar plano, String cpfLogado) throws PersistenciaException {
        dao.inserir(plano);
        historicoService.registrar(
                TipoHistorico.PLANOALIMENTAR,
                TipoHistorico.PLANOALIMENTAR.getDescricao(plano.getAnimal()),
                cpfLogado
        );
    }
}
