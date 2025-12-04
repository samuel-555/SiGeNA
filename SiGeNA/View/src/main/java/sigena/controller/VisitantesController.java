package sigena.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
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
public class VisitantesController extends HttpServlet {

    private final GestaoVisitaService service = new GestaoVisitaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        HttpSession sessao = req.getSession();

        try {
            if ("excluir".equalsIgnoreCase(acao)) {
                Long id = Long.valueOf(req.getParameter("id"));
                service.excluir(id);
                sessao.setAttribute("mensagemSucesso", "Visita removida com sucesso.");
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
                service.registrarVisita(visita);
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
                sessao.setAttribute("mensagemSucesso", "Visita removida com sucesso.");
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
        String ordenacao = req.getParameter("ordenacao");
        if (ordenacao == null || ordenacao.isBlank()) {
            ordenacao = "recentes";
        }

        LocalDate inicio = parseData(req.getParameter("inicio"));
        LocalDate fim = parseData(req.getParameter("fim"));

        List<Visita> visitas = service.listar(ordenacao, inicio, fim);

        req.setAttribute("visitas", visitas);
        req.setAttribute("ordenacaoSelecionada", ordenacao);
        req.setAttribute("inicioFiltro", inicio);
        req.setAttribute("fimFiltro", fim);

        Object dados = req.getSession().getAttribute("dadosFormulario");
        if (dados != null) {
            req.setAttribute("dadosFormulario", dados);
            req.getSession().removeAttribute("dadosFormulario");
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
}
