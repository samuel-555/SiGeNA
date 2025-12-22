package sigena.model.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import sigena.model.common.exception.DataInvalidaException;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.TarefaDAO;
import sigena.model.domain.Tarefa;
import sigena.model.domain.util.TipoHistorico;

public class GestaoTarefaService {

    private final TarefaDAO dao;
    private final GestaoHistoricoService historicoService;

    public GestaoTarefaService() {
        this.dao = new TarefaDAO();
        this.historicoService = new GestaoHistoricoService();
    }

    public void cadastrarTarefa(String nome, String texto, int id_destinatario, LocalDateTime dataPConclusao, String cpfAutor) throws DataInvalidaException {
        if (!validarData(dataPConclusao)) {
            throw new DataInvalidaException("A data de conclusão deve ser posterior ao momento atual.");
        }

        Tarefa tarefa = new Tarefa(nome, texto, id_destinatario, dataPConclusao, cpfAutor);
        dao.inserir(tarefa);
    }

    public List<Tarefa> listarTarefas() {
        return dao.listar();
    }

    public List<Tarefa> listarPorUsuario(int id) throws PersistenciaException, SQLException {
        return dao.listarPorUsuario(id);
    }

    public void editar(long id, String nome, String texto, int idDestinatario, LocalDateTime dataPConclusao, String cpfAutor, String cpfLogado)
            throws DataInvalidaException {

        if (!validarData(dataPConclusao)) {
            throw new DataInvalidaException("A data de conclusão deve ser posterior ao momento atual.");
        }

        Tarefa tarefaBanco = dao.buscar(id);

        if (tarefaBanco == null) {
            throw new IllegalArgumentException("Tarefa não encontrada.");
        }

        if (!tarefaBanco.getCpfAutor().equals(cpfLogado)) {
            throw new SecurityException("Permissão negada: você não é o autor desta tarefa.");
        }

        tarefaBanco.setNome(nome);
        tarefaBanco.setTexto(texto);
        tarefaBanco.setIdDestinatario(idDestinatario);
        tarefaBanco.setDataPConclusao(dataPConclusao);

        dao.editar(id, tarefaBanco);
    }

    public void editarConcluida(long id, boolean concluida, String cpf) {
        Tarefa tarefa = dao.buscar(id);
        if (tarefa == null) {
            throw new IllegalArgumentException("Tarefa não encontrada.");
        }

        dao.editarConcluida(id, concluida);

        if (concluida) {
            historicoService.registrar(TipoHistorico.TAREFA, TipoHistorico.TAREFA.getDescricao(tarefa), cpf);
        }
    }

    public Tarefa buscar(long id) {
        return dao.buscar(id);
    }

    public void excluir(Tarefa tarefa, String cpfLogado) {
        if (!tarefa.getCpfAutor().equals(cpfLogado)) {
            throw new SecurityException("Permissão negada: você não pode excluir uma tarefa que não criou.");
        }

        dao.excluir(tarefa);
    }

    public boolean validarData(LocalDateTime data) {
        if (data == null) {
            return false;
        }
        return data.isAfter(LocalDateTime.now());
    }

    public List<Tarefa> listarTarefasDoDia() {
        return dao.listarDoDia();
    }

    public List<Tarefa> listarTarefasDoDiaPorUsuario(int id) {
        return dao.listarDoDiaPorUsuario(id);
    }

    public List<Tarefa> listarTarefasDoDiaPorCpf(String cpf) {
        return dao.listarDoDiaPorCpf(cpf);
    }
}
