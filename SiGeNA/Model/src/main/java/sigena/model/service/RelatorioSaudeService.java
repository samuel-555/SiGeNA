package sigena.model.service;

import java.time.LocalDate;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.AnimalDAO;
import sigena.model.dao.RelatorioSaudeDAO;
import sigena.model.domain.Animal;
import sigena.model.domain.RelatorioSaude;

public class RelatorioSaudeService {

    private final RelatorioSaudeDAO relatorioDAO = new RelatorioSaudeDAO();
    private final AnimalDAO animalDAO = new AnimalDAO();

    public RelatorioSaude registrarCheckup(Long animalId, LocalDate data, Double peso, String status, String observacoes)
            throws PersistenciaException {
        validarDadosBasicos(animalId, data, status);
        Animal animal = buscarAnimalValido(animalId);

        RelatorioSaude relatorio = new RelatorioSaude(animal, data, validarPeso(peso), status.trim(), observacoes);
        return relatorioDAO.cadastrar(relatorio);
    }

    public void atualizarRelatorio(Long relatorioId, Long animalId, LocalDate data, Double peso, String status, String observacoes)
            throws PersistenciaException {
        if (relatorioId == null || relatorioId <= 0) {
            throw new PersistenciaException("Relatório inválido para edição.");
        }
        validarDadosBasicos(animalId, data, status);

        RelatorioSaude existente = relatorioDAO.buscarPorId(relatorioId);
        if (existente == null) {
            throw new PersistenciaException("Relatório não encontrado para edição.");
        }

        Animal animal = buscarAnimalValido(animalId);
        existente.setAnimal(animal);
        existente.setDataRelatorio(data);
        existente.setPeso(validarPeso(peso));
        existente.setStatus(status.trim());
        existente.setObservacoes(observacoes);

        relatorioDAO.atualizar(existente);
    }

    public List<RelatorioSaude> consultarHistorico(Long animalId) throws PersistenciaException {
        if (animalId == null || animalId <= 0) {
            throw new PersistenciaException("Animal inválido para consulta de histórico.");
        }
        return relatorioDAO.listarPorAnimal(animalId);
    }

    public List<RelatorioSaude> listarTodos() throws PersistenciaException {
        return relatorioDAO.listarTodos();
    }

    public RelatorioSaude buscarPorId(Long id) throws PersistenciaException {
        if (id == null || id <= 0) {
            throw new PersistenciaException("Relatório inválido.");
        }

        RelatorioSaude relatorio = relatorioDAO.buscarPorId(id);
        if (relatorio == null) {
            throw new PersistenciaException("Relatório não encontrado.");
        }
        return relatorio;
    }

    public void acrescentarObservacao(Long relatorioId, String novaObservacao) throws PersistenciaException {
        if (relatorioId == null || relatorioId <= 0) {
            throw new PersistenciaException("Relatório inválido para atualização.");
        }
        if (novaObservacao == null || novaObservacao.isBlank()) {
            throw new PersistenciaException("Observação não pode ser vazia.");
        }
        relatorioDAO.acrescentarObservacao(relatorioId, novaObservacao.trim());
    }

    public void excluirRelatorio(Long relatorioId) throws PersistenciaException {
        if (relatorioId == null || relatorioId <= 0) {
            throw new PersistenciaException("Relatório inválido para exclusão.");
        }
        relatorioDAO.excluir(relatorioId);
    }

    private void validarDadosBasicos(Long animalId, LocalDate data, String status) throws PersistenciaException {
        if (animalId == null || animalId <= 0) {
            throw new PersistenciaException("Animal inválido para o relatório.");
        }
        if (data == null) {
            throw new PersistenciaException("A data do check-up é obrigatória.");
        }
        if (status == null || status.isBlank()) {
            throw new PersistenciaException("O status do animal deve ser informado.");
        }
    }

    private Animal buscarAnimalValido(Long animalId) throws PersistenciaException {
        Animal animal = animalDAO.buscarPorId(animalId);
        if (animal == null) {
            throw new PersistenciaException("Animal informado não foi encontrado.");
        }
        return animal;
    }

    private Double validarPeso(Double peso) throws PersistenciaException {
        if (peso == null) {
            return null;
        }
        if (peso <= 0) {
            throw new PersistenciaException("Peso informado inválido.");
        }
        return peso;
    }
}
