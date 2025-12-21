package sigena.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.exception.ValidationException;
import sigena.model.domain.Visita;
import sigena.model.service.GestaoVisitaService;

@WebServlet("/visitantes")
public class VisitantesController extends Controller {

    private final GestaoVisitaService service = new GestaoVisitaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        HttpSession sessao = req.getSession();

        try {
            if ("excluir".equalsIgnoreCase(acao)) {
                Long id = Long.valueOf(req.getParameter("id"));
                service.excluir(id);
                sessao.setAttribute("mensagemSucesso", "Visita cancelada com sucesso.");
                resp.sendRedirect("visitantes");
                return;
            }

            if ("editar".equalsIgnoreCase(acao)) {
                Long id = Long.valueOf(req.getParameter("id"));
                Visita visita = service.buscarPorId(id);
                req.setAttribute("visitaEdicao", visita);
            }

            carregarMensagens(sessao, req);
            carregarVisitas(req);
            req.getRequestDispatcher("visitantes.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Erro ao processar visitas: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        HttpSession sessao = req.getSession();

        try {
            if ("cadastrar".equalsIgnoreCase(acao)) {
                Visita visita = construirVisita(req, false);
                String cpfLogado = getCpfUsuarioLogado(req);
                service.registrarVisita(visita, cpfLogado);
                sessao.setAttribute("mensagemSucesso", "Visita registrada com sucesso.");
                resp.sendRedirect("visitantes");
                return;
            }

            if ("atualizar".equalsIgnoreCase(acao)) {
                Visita visita = construirVisita(req, true);
                service.atualizar(visita);
                sessao.setAttribute("mensagemSucesso", "Visita atualizada com sucesso.");
                resp.sendRedirect("visitantes");
                return;
            }

            if ("excluir".equalsIgnoreCase(acao)) {
                Long id = Long.valueOf(req.getParameter("id"));
                service.excluir(id);
                sessao.setAttribute("mensagemSucesso", "Visita cancelada com sucesso.");
                resp.sendRedirect("visitantes");
                return;
            }

            resp.sendRedirect("visitantes");
        } catch (ValidationException e) {
            sessao.setAttribute("mensagemErro", e.getMessage());
            sessao.setAttribute("dadosFormulario", capturarFormulario(req));
            resp.sendRedirect("visitantes");
        } catch (PersistenciaException e) {
            throw new ServletException("Erro na camada de dados: " + e.getMessage(), e);
        }
    }

    private void carregarVisitas(HttpServletRequest req) throws PersistenciaException {
        HttpSession session = req.getSession();
        String ordenacao = paramOuSessao(req, session, "ordenacao", "recentes");
        LocalDate inicio = parseDataOuSessao(req, session, "inicio");
        LocalDate fim = parseDataOuSessao(req, session, "fim");
        String busca = paramOuSessao(req, session, "busca", "");

        List<Visita> visitas = service.listar(ordenacao, inicio, fim, busca);

        session.setAttribute("filtroVisitantes_ordenacao", ordenacao);
        session.setAttribute("filtroVisitantes_inicio", inicio);
        session.setAttribute("filtroVisitantes_fim", fim);
        session.setAttribute("filtroVisitantes_busca", busca);

        req.setAttribute("visitas", visitas);
        req.setAttribute("ordenacaoSelecionada", ordenacao);
        req.setAttribute("inicioFiltro", inicio);
        req.setAttribute("fimFiltro", fim);
        req.setAttribute("buscaFiltro", busca);
        req.setAttribute("totalVisitas", service.contarTotal());
        req.setAttribute("visitasHoje", service.contarHoje());
        req.setAttribute("totalFiltrado", visitas != null ? visitas.size() : 0);

        Object dados = session.getAttribute("dadosFormulario");
        if (dados != null) {
            req.setAttribute("dadosFormulario", dados);
            session.removeAttribute("dadosFormulario");
        }
    }

    private void carregarMensagens(HttpSession sessao, HttpServletRequest req) {
        Object sucesso = sessao.getAttribute("mensagemSucesso");
        if (sucesso != null) {
            req.setAttribute("mensagemSucesso", sucesso);
            sessao.removeAttribute("mensagemSucesso");
        }

        Object erro = sessao.getAttribute("mensagemErro");
        if (erro != null) {
            req.setAttribute("mensagemErro", erro);
            sessao.removeAttribute("mensagemErro");
        }
    }

    private Visita construirVisita(HttpServletRequest req, boolean atualizar) {
        Visita visita = new Visita();
        if (atualizar) {
            visita.setId(Long.valueOf(req.getParameter("id")));
        }

        visita.setNomeVisitante(req.getParameter("nome"));
        visita.setDocumento(req.getParameter("documento"));
        visita.setMotivo(req.getParameter("motivo"));
        visita.setDataVisita(parseData(req.getParameter("dataVisita")));
        visita.setObservacoes(req.getParameter("observacoes"));
        visita.setVip(req.getParameter("vip") != null);
        boolean necessidade = req.getParameter("necessidadeEspecial") != null;
        visita.setNecessidadeEspecial(necessidade);
        if (necessidade) {
            visita.setDescricaoNecessidade(req.getParameter("descricaoNecessidade"));
        } else {
            visita.setDescricaoNecessidade(null);
        }
        try {
            String turnoParam = req.getParameter("turno");
            if (turnoParam != null && !turnoParam.isBlank()) {
                visita.setTurno(sigena.model.domain.Turno.valueOf(turnoParam));
            }
        } catch (Exception ignored) {
            visita.setTurno(null);
        }
        return visita;
    }

    private LocalDate parseData(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(data);
        } catch (Exception e) {
            return null;
        }
    }

    private Visita capturarFormulario(HttpServletRequest req) {
        Visita v = new Visita();
        v.setNomeVisitante(req.getParameter("nome"));
        v.setDocumento(req.getParameter("documento"));
        v.setMotivo(req.getParameter("motivo"));
        v.setDataVisita(parseData(req.getParameter("dataVisita")));
        v.setObservacoes(req.getParameter("observacoes"));
        return v;
    }

    private String paramOuSessao(HttpServletRequest req, HttpSession session, String nomeParam, String padrao) {
        String p = req.getParameter(nomeParam);
        if (p != null) {
            return p;
        }
        Object salvo = session.getAttribute("filtroVisitantes_" + nomeParam);
        return salvo != null ? String.valueOf(salvo) : padrao;
    }

    private LocalDate parseDataOuSessao(HttpServletRequest req, HttpSession session, String nomeParam) {
        LocalDate data = parseData(req.getParameter(nomeParam));
        if (data != null) return data;
        Object salvo = session.getAttribute("filtroVisitantes_" + nomeParam);
        if (salvo instanceof LocalDate) {
            return (LocalDate) salvo;
        }
        try {
            if (salvo != null) {
                return LocalDate.parse(String.valueOf(salvo));
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
