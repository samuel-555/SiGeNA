package sigena.model.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.exception.ValidationException;
import sigena.model.dao.AgendamentoDAO;
import sigena.model.domain.Agendamento;
import sigena.model.domain.AgendamentoStatus;

public class GestaoAgendamentoService {

    private final AgendamentoDAO dao;

    public GestaoAgendamentoService() {
        this.dao = new AgendamentoDAO();
    }

    public Agendamento criarAgendamento(Agendamento agendamento) throws PersistenciaException, ValidationException {
        validarAgendamento(agendamento, null);
        agendamento.setStatus(AgendamentoStatus.ATIVO);
        dao.inserir(agendamento);
        return agendamento;
    }

    public void atualizarAgendamento(Agendamento agendamento) throws PersistenciaException, ValidationException {
        if (agendamento.getId() == null) {
            throw new ValidationException("Agendamento invalido.");
        }
        validarAgendamento(agendamento, agendamento.getId());
        dao.atualizar(agendamento);
    }

    public void cancelarAgendamento(Long id) throws PersistenciaException, ValidationException {
        Agendamento agendamento = dao.buscarPorId(id);
        if (agendamento == null) {
            throw new ValidationException("Agendamento nao encontrado.");
        }

        if (AgendamentoStatus.CANCELADO.equals(agendamento.getStatus())) {
            return;
        }

        LocalDateTime dataHora = agendamento.getDataHora();
        if (dataHora == null) {
            throw new ValidationException("Data ou hora do agendamento nao informada.");
        }

        Duration ateInicio = Duration.between(LocalDateTime.now(), dataHora);
        if (ateInicio.toHours() < 24) {
            throw new ValidationException("Cancelamentos precisam ser feitos com no minimo 24h de antecedencia.");
        }

        dao.cancelar(id);
    }

    public List<Agendamento> listarAgendamentos() throws PersistenciaException {
        return dao.listar();
    }

    public Agendamento buscarPorId(Long id) throws PersistenciaException {
        return dao.buscarPorId(id);
    }

    private void validarAgendamento(Agendamento agendamento, Long idIgnorado) throws ValidationException, PersistenciaException {
        if (agendamento == null) {
            throw new ValidationException("Agendamento nao informado.");
        }

        if (agendamento.getTipo() == null || agendamento.getTipo().isBlank()) {
            throw new ValidationException("Informe o tipo do agendamento.");
        }

        if (agendamento.getResponsavel() == null || agendamento.getResponsavel().isBlank()) {
            throw new ValidationException("Informe o responsavel pelo agendamento.");
        }

        if (agendamento.getLocal() == null || agendamento.getLocal().isBlank()) {
            throw new ValidationException("Informe o local do agendamento.");
        }

        LocalDateTime dataHora = agendamento.getDataHora();
        if (dataHora == null) {
            throw new ValidationException("Informe data e hora do agendamento.");
        }

        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Agendamentos devem ser criados para datas futuras.");
        }

        agendamento.setTipo(agendamento.getTipo().trim());
        agendamento.setResponsavel(agendamento.getResponsavel().trim());
        agendamento.setLocal(agendamento.getLocal().trim());
        if (agendamento.getObservacoes() != null) {
            agendamento.setObservacoes(agendamento.getObservacoes().trim());
        }

        boolean conflito = dao.existeConflito(
                agendamento.getData(),
                agendamento.getHora(),
                agendamento.getResponsavel(),
                agendamento.getLocal(),
                idIgnorado);

        if (conflito) {
            throw new ValidationException("Ja existe agendamento ativo nesse horario para o responsavel ou local informado.");
        }
    }
}
