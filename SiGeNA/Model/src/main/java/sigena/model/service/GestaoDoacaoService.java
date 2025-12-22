package sigena.model.service;

import java.util.List;
import sigena.model.common.exception.DatabaseException;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.exception.ValidationException;
import sigena.model.dao.DoacaoDAO;
import sigena.model.dao.ReciboDoacaoDAO;
import sigena.model.domain.Doacao;
import sigena.model.domain.ReciboDoacao;
import sigena.model.domain.util.StatusDoacao;
import sigena.model.domain.util.DoacaoTipo;


public class GestaoDoacaoService {

    private static final double LIMITE_RECIBO = 10000.0;

    private final DoacaoDAO doacaoDAO;
    private final ReciboDoacaoDAO reciboDAO;

    public GestaoDoacaoService() {
        this.doacaoDAO = new DoacaoDAO();
        this.reciboDAO = new ReciboDoacaoDAO();
    }

    public Doacao registrarDoacao(Doacao doacao) throws PersistenciaException, ValidationException {
        validarDoacao(doacao);
        doacao.setStatus(StatusDoacao.ATIVA);
        doacao.setReciboEmitido(false);

        try {
            doacaoDAO.salvar(doacao);
            if (deveEmitirRecibo(doacao)) {
                reciboDAO.emitirRecibo(doacao.getId());
                doacaoDAO.atualizarReciboEmitido(doacao.getId(), true);
                doacao.setReciboEmitido(true);
            }
            return doacao;
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public List<Doacao> listarDoacoes() throws PersistenciaException {
        try {
            return doacaoDAO.listarTodas();
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public Doacao buscarPorId(Long id) throws PersistenciaException {
        try {
            return doacaoDAO.buscarPorId(id);
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public void atualizarValor(Long id, Double valor) throws PersistenciaException, ValidationException {
        if (valor == null || valor <= 0) {
            throw new ValidationException("Informe um valor válido para a doação monetária.");
        }

        Doacao doacao = buscarPorId(id);
        if (doacao == null) {
            throw new ValidationException("Doação não encontrada.");
        }
        if (!DoacaoTipo.MONETARIA.equals(doacao.getTipo())) {
            throw new ValidationException("Apenas doações monetárias permitem atualização de valor.");
        }

        try {
            doacaoDAO.atualizarValor(id, valor);
            doacao.setValorMonetario(valor);
            if (deveEmitirRecibo(doacao) && !doacao.isReciboEmitido()) {
                reciboDAO.emitirRecibo(doacao.getId());
                doacaoDAO.atualizarReciboEmitido(doacao.getId(), true);
            }
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public void atualizarDescricao(Long id, String descricao) throws PersistenciaException, ValidationException {
        if (descricao == null || descricao.isBlank()) {
            throw new ValidationException("Informe uma descrição para a doação.");
        }

        Doacao doacao = buscarPorId(id);
        if (doacao == null) {
            throw new ValidationException("Doação não encontrada.");
        }
        if (!DoacaoTipo.OUTRO.equals(doacao.getTipo())) {
            throw new ValidationException("Somente doações do tipo 'Outro' permitem atualização da descrição.");
        }

        try {
            doacaoDAO.atualizarDescricao(id, descricao);
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public void cancelarDoacao(Long id) throws PersistenciaException, ValidationException {
        Doacao doacao = buscarPorId(id);
        if (doacao == null) {
            throw new ValidationException("Doação não encontrada.");
        }
        if (StatusDoacao.CANCELADA.equals(doacao.getStatus())) {
            return;
        }

        try {
            doacaoDAO.atualizarStatus(id, StatusDoacao.CANCELADA);
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public ReciboDoacao buscarRecibo(Long doacaoId) throws PersistenciaException {
        try {
            return reciboDAO.buscarUltimoPorDoacao(doacaoId);
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    private void validarDoacao(Doacao doacao) throws ValidationException {
        if (doacao.getNomeDoador() == null || doacao.getNomeDoador().isBlank()) {
            throw new ValidationException("Informe o nome do doador.");
        }
        if (doacao.getDataDoacao() == null) {
            throw new ValidationException("Informe a data da doação.");
        }

        if (DoacaoTipo.MONETARIA.equals(doacao.getTipo())) {
            if (doacao.getValorMonetario() == null || doacao.getValorMonetario() <= 0) {
                throw new ValidationException("Informe um valor válido para a doação monetária.");
            }
        } else {
            if (doacao.getDescricaoOutro() == null || doacao.getDescricaoOutro().isBlank()) {
                throw new ValidationException("Informe a descrição da contribuição.");
            }
        }
    }

    private boolean deveEmitirRecibo(Doacao doacao) {
        return DoacaoTipo.MONETARIA.equals(doacao.getTipo())
                && doacao.getValorMonetario() != null
                && doacao.getValorMonetario() >= LIMITE_RECIBO;
    }
}
