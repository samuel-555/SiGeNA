package sigena.model.service;

import sigena.model.dao.ReciboDoacaoDAO;
import sigena.model.domain.ReciboDoacao;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReciboDoacaoService {

    private final ReciboDoacaoDAO reciboDAO;

    public ReciboDoacaoService(ReciboDoacaoDAO reciboDAO) {
        this.reciboDAO = reciboDAO;
    }

    public ReciboDoacao emitirRecibo(Long doacaoId) throws Exception {

        ReciboDoacao existente = reciboDAO.buscarPorDoacao(doacaoId);
        if (existente != null) {
            return existente;
        }

        ReciboDoacao r = new ReciboDoacao();
        r.setDoacaoId(doacaoId);
        r.setCodigo("REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        r.setDataEmissao(LocalDateTime.now());

        reciboDAO.salvar(r);

        return r;
    }
}
