package sigena.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Animal;
import sigena.model.domain.Cargo;
import sigena.model.domain.RelatorioSaude;
import sigena.model.service.GestaoAnimalService;
import sigena.model.service.RelatorioSaudeService;

@WebServlet(name = "RelatorioSaudeController", urlPatterns = {"/RelatorioSaudeController"})
public class RelatorioSaudeController extends Controller {

    private final RelatorioSaudeService relatorioService = new RelatorioSaudeService();
    private final GestaoAnimalService animalService = new GestaoAnimalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            boolean usuarioGerente = isGerente(request);
            request.setAttribute("usuarioGerente", usuarioGerente);
            carregarMensagens(request);
            carregarDadosPagina(request, usuarioGerente);
            request.getRequestDispatcher("relatorios-saude.jsp").forward(request, response);
        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("relatorios-saude.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (!isGerente(request)) {
            redirecionarComMensagem(request, response, "Usuario sem permissão para alterar relatórios.", true);
            return;
        }

        String acao = request.getParameter("acao");

        try {
            if ("criar".equals(acao)) {
                salvarRelatorio(request);
                redirecionarComMensagem(request, response, "Relatório registrado com sucesso.", false);
                return;
            }
            if ("atualizar".equals(acao)) {
                atualizarRelatorio(request);
                redirecionarComMensagem(request, response, "Relatório atualizado com sucesso.", false);
                return;
            }
            if ("adicionarObservacao".equals(acao)) {
                adicionarObservacao(request);
                redirecionarComMensagem(request, response, "Observação acrescentada.", false);
                return;
            }
            if ("excluir".equals(acao)) {
                excluirRelatorio(request);
                redirecionarComMensagem(request, response, "Relatório cancelado.", false);
                return;
            }

            redirecionarComMensagem(request, response, "Ação inválida.", true);
        } catch (PersistenciaException | IllegalArgumentException e) {
            redirecionarComMensagem(request, response, e.getMessage(), true);
        }
    }

    private void carregarDadosPagina(HttpServletRequest request, boolean usuarioGerente) throws PersistenciaException {
        List<Animal> animais = animalService.listarAnimais();
        request.setAttribute("animais", animais);

        String acao = request.getParameter("acao");
        Long animalId = parseLong(request.getParameter("animalId"));
        String statusFiltro = normalizarStatus(request.getParameter("statusFiltro"));

        if ("editar".equals(acao) && usuarioGerente) {
            Long relatorioId = parseLong(request.getParameter("id"));
            if (relatorioId != null) {
                RelatorioSaude relatorio = relatorioService.buscarPorId(relatorioId);
                request.setAttribute("relatorioEdicao", relatorio);
                request.setAttribute("animalSelecionado", relatorio.getAnimal().getId());
            }
        } else if ("historico".equals(acao)) {
            request.setAttribute("animalSelecionado", animalId);
        }

        List<RelatorioSaude> relatorios = relatorioService.listarFiltrado(animalId, statusFiltro);
        request.setAttribute("statusSelecionado", statusFiltro);
        request.setAttribute("relatorios", relatorios);
    }

    private void salvarRelatorio(HttpServletRequest request) throws PersistenciaException {
        Long animalId = parseRequiredLong(request.getParameter("animalId"), "Animal obrigatório.");
        LocalDate data = parseData(request.getParameter("dataRelatorio"));
        Double peso = parsePeso(request.getParameter("peso"));
        boolean apto = isAptoMarcado(request.getParameter("apto"));
        String observacoes = request.getParameter("observacoes");
        String cpfLogado = getCpfUsuarioLogado(request);
        relatorioService.registrarCheckup(animalId, data, peso, apto, observacoes, cpfLogado);
    }

    private void atualizarRelatorio(HttpServletRequest request) throws PersistenciaException {
        Long relatorioId = parseRequiredLong(request.getParameter("relatorioId"), "Relatório não informado.");
        Long animalId = parseRequiredLong(request.getParameter("animalId"), "Animal obrigatório.");
        LocalDate data = parseData(request.getParameter("dataRelatorio"));
        Double peso = parsePeso(request.getParameter("peso"));
        boolean apto = isAptoMarcado(request.getParameter("apto"));
        String observacoes = request.getParameter("observacoes");
        relatorioService.atualizarRelatorio(relatorioId, animalId, data, peso, apto, observacoes);
    }

    private void adicionarObservacao(HttpServletRequest request) throws PersistenciaException {
        Long relatorioId = parseRequiredLong(request.getParameter("relatorioObservacaoId"), "Selecione um relatório.");
        String observacao = request.getParameter("novaObservacao");
        relatorioService.acrescentarObservacao(relatorioId, observacao);
    }

    private void excluirRelatorio(HttpServletRequest request) throws PersistenciaException {
        Long relatorioId = parseRequiredLong(request.getParameter("relatorioId"), "Relatório não informado.");
        relatorioService.excluirRelatorio(relatorioId);
    }

    private void carregarMensagens(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        Object sucesso = session.getAttribute("mensagemSucesso");
        if (sucesso != null) {
            request.setAttribute("mensagemSucesso", sucesso.toString());
            session.removeAttribute("mensagemSucesso");
        }
        Object erro = session.getAttribute("mensagemErro");
        if (erro != null) {
            request.setAttribute("mensagemErro", erro.toString());
            session.removeAttribute("mensagemErro");
        }
    }

    private void redirecionarComMensagem(HttpServletRequest request, HttpServletResponse response, String mensagem, boolean erro)
            throws IOException {
        HttpSession session = request.getSession();
        if (erro) {
            session.setAttribute("mensagemErro", mensagem);
        } else {
            session.setAttribute("mensagemSucesso", mensagem);
        }
        response.sendRedirect(request.getContextPath() + "/RelatorioSaudeController");
    }

    private boolean isGerente(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao == null) {
            return false;
        }
        Object cargo = sessao.getAttribute("cargoUsuario");
        return cargo instanceof Cargo && cargo == Cargo.GERENTE;
    }

    private Long parseLong(String valor) {
        try {
            if (valor == null || valor.isBlank()) {
                return null;
            }
            return Long.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseRequiredLong(String valor, String mensagemErro) {
        Long resultado = parseLong(valor);
        if (resultado == null) {
            throw new IllegalArgumentException(mensagemErro);
        }
        return resultado;
    }

    private LocalDate parseData(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Informe a data do check-up.");
        }
        return LocalDate.parse(valor);
    }

    private Double parsePeso(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Double.valueOf(valor.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Peso informado inválido.");
        }
    }

    private boolean isAptoMarcado(String valor) {
        if (valor == null || valor.isBlank()) {
            return false;
        }
        String normalizado = valor.trim().toUpperCase();
        return "APTO".equals(normalizado) || "TRUE".equals(normalizado) || "ON".equals(normalizado);
    }

    private String normalizarStatus(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String normalizado = valor.trim().toUpperCase();
        if (!"APTO".equals(normalizado) && !"INAPTO".equals(normalizado)) {
            return null;
        }
        return normalizado;
    }
}
