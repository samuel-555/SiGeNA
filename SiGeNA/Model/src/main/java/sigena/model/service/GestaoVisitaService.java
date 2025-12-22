package sigena.model.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import sigena.model.common.exception.DatabaseException;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.exception.ValidationException;
import sigena.model.dao.VisitaDAO;
import sigena.model.domain.util.TipoHistorico;
import sigena.model.domain.Visita;
import sigena.model.util.ConexaoDB;
import sigena.model.util.InitDB;

public class GestaoVisitaService {

    private static final String STATUS_CANCELADA = "CANCELADA";

    private final VisitaDAO dao = new VisitaDAO();
    private final GestaoHistoricoService historicoService = new GestaoHistoricoService();

    public GestaoVisitaService() {
        inicializarTabela();
    }

    public void registrarVisita(Visita visita) throws PersistenciaException, ValidationException {
        validar(visita);
        try {
            dao.salvar(visita);
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public void registrarVisita(Visita visita, String cpfLogado) throws PersistenciaException, ValidationException {
        registrarVisita(visita);
        registrarHistorico(visita, cpfLogado);
    }

    public List<Visita> listar(String ordenacao, LocalDate inicio, LocalDate fim, String busca) throws PersistenciaException {
        try {
            return dao.listar(ordenacao, inicio, fim, busca);
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public long contarTotal() throws PersistenciaException {
        try {
            return dao.contarTodas();
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public long contarHoje() throws PersistenciaException {
        try {
            return dao.contarHoje();
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public Visita buscarPorId(Long id) throws PersistenciaException {
        try {
            return dao.buscarPorId(id);
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public void atualizar(Visita visita) throws PersistenciaException, ValidationException {
        if (visita.getId() == null) {
            throw new ValidationException("Visita não encontrada.");
        }
        Visita existente = buscarPorId(visita.getId());
        if (existente == null) {
            throw new ValidationException("Visita não encontrada.");
        }
        if (STATUS_CANCELADA.equalsIgnoreCase(existente.getStatus())) {
            throw new ValidationException("Visita cancelada não pode ser editada.");
        }
        validar(visita);
        try {
            dao.atualizar(visita);
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    public void excluir(Long id) throws PersistenciaException {
        try {
            Visita visita = dao.buscarPorId(id);
            if (visita == null) {
                throw new PersistenciaException("Visita não encontrada.");
            }
            if (STATUS_CANCELADA.equalsIgnoreCase(visita.getStatus())) {
                throw new PersistenciaException("Visita já cancelada.");
            }
            dao.excluir(id);
        } catch (DatabaseException e) {
            throw new PersistenciaException(e.getMessage());
        }
    }

    private void inicializarTabela() {
        try (Connection con = ConexaoDB.getConnection()) {
            new InitDB(con).initVisitas();
        } catch (SQLException ignored) {
        }
    }

    private void validar(Visita visita) throws ValidationException {
        if (visita.getNomeVisitante() == null || visita.getNomeVisitante().isBlank()) {
            throw new ValidationException("Informe o nome do visitante.");
        }
        if (visita.getMotivo() == null || visita.getMotivo().isBlank()) {
            throw new ValidationException("Informe o motivo da visita.");
        }
        if (visita.getDataVisita() == null) {
            throw new ValidationException("Informe a data da visita.");
        }
        if (visita.getTurno() == null) {
            throw new ValidationException("Selecione o turno da visita.");
        }
    }

    private void registrarHistorico(Visita visita, String cpfLogado) {
        Date dataVisita = visita.getDataVisita() != null ? Date.valueOf(visita.getDataVisita()) : null;
        historicoService.registrar(
                TipoHistorico.VISITA,
                TipoHistorico.VISITA.getDescricao(
                        visita.getNomeVisitante(),
                        visita.getMotivo(),
                        dataVisita,
                        visita.getObservacoes(),
                        visita.isVip()
                ),
                cpfLogado
        );
    }
}
