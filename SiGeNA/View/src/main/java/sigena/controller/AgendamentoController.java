package sigena.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.exception.ValidationException;
import sigena.model.domain.Agendamento;
import sigena.model.service.GestaoAgendamentoService;

@WebServlet(name = "AgendamentoController", urlPatterns = {"/AgendamentoController"})
public class AgendamentoController extends HttpServlet {

    private final GestaoAgendamentoService service = new GestaoAgendamentoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if (acao == null || "listar".equals(acao)) {
            listar(request, response);
            return;
        }

        if ("ver".equals(acao)) {
            try {
                Long id = Long.valueOf(request.getParameter("id"));
                Agendamento agendamento = service.buscarPorId(id);
                if (agendamento == null) {
                    HttpSession sessao = request.getSession();
                    sessao.setAttribute("campoInvalidoErro", "Agendamento nao encontrado.");
                    response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=listar");
                    return;
                }
                request.setAttribute("agendamento", agendamento);
                request.getRequestDispatcher("ver-agendamento.jsp").forward(request, response);
                return;
            } catch (PersistenciaException e) {
                throw new ServletException("Erro ao carregar agendamento: " + e.getMessage(), e);
            }
        }

        if ("cadastrar".equals(acao)) {
            request.getRequestDispatcher("cadastrar-agendamento.jsp").forward(request, response);
            return;
        }

        if ("editar".equals(acao)) {
            try {
                Long id = Long.valueOf(request.getParameter("id"));
                Agendamento agendamento = service.buscarPorId(id);
                if (agendamento == null) {
                    HttpSession sessao = request.getSession();
                    sessao.setAttribute("campoInvalidoErro", "Agendamento nao encontrado.");
                    response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=listar");
                    return;
                }
                if (agendamento.getStatus() != null && "CANCELADO".equals(agendamento.getStatus().name())) {
                    HttpSession sessao = request.getSession();
                    sessao.setAttribute("campoInvalidoErro", "Agendamento cancelado nao pode ser editado.");
                    response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=listar");
                    return;
                }
                request.setAttribute("agendamento", agendamento);
                request.getRequestDispatcher("cadastrar-agendamento.jsp").forward(request, response);
                return;
            } catch (PersistenciaException e) {
                throw new ServletException("Erro ao carregar agendamento: " + e.getMessage(), e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=listar");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        HttpSession sessao = request.getSession();

        try {
            if ("salvar".equals(acao)) {
                Agendamento agendamento = montarAgendamento(request, false);
                service.criarAgendamento(agendamento);
                sessao.setAttribute("acaoBemSucedida", "Agendamento cadastrado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=listar");
                return;
            }

            if ("atualizar".equals(acao)) {
                Agendamento agendamento = montarAgendamento(request, true);
                service.atualizarAgendamento(agendamento);
                sessao.setAttribute("acaoBemSucedida", "Agendamento atualizado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=listar");
                return;
            }

            if ("cancelar".equals(acao)) {
                Long id = Long.valueOf(request.getParameter("id"));
                String justificativa = request.getParameter("justificativa");
                service.cancelarAgendamento(id, justificativa);
                sessao.setAttribute("acaoBemSucedida", "Agendamento cancelado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=listar");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=listar");
        } catch (ValidationException e) {
            sessao.setAttribute("campoInvalidoErro", e.getMessage());
            if ("atualizar".equals(acao)) {
                String id = request.getParameter("id");
                response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=editar&id=" + id);
            } else if ("cancelar".equals(acao)) {
                response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=listar");
            } else {
                response.sendRedirect(request.getContextPath() + "/AgendamentoController?acao=cadastrar");
            }
        } catch (PersistenciaException e) {
            throw new ServletException("Erro ao processar agendamento: " + e.getMessage(), e);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Agendamento> agendamentos = service.listarAgendamentos();

            String busca = parametroOuVazio(request.getParameter("busca"));
            String tipoFiltro = parametroOuVazio(request.getParameter("tipo"));
            String ordem = parametroOuVazio(request.getParameter("ordem"));

            if (!busca.isBlank()) {
                String termo = busca.toLowerCase();
                agendamentos = agendamentos.stream()
                        .filter(a -> a.getResponsavel() != null && a.getResponsavel().toLowerCase().contains(termo))
                        .toList();
            }

            if (!tipoFiltro.isBlank()) {
                String filtro = tipoFiltro.toLowerCase();
                agendamentos = agendamentos.stream()
                        .filter(a -> a.getTipo() != null && a.getTipo().toLowerCase().equals(filtro))
                        .toList();
            }

            if (!ordem.isBlank()) {
                Comparator<Agendamento> porNome = Comparator.comparing(a -> a.getResponsavel() == null ? "" : a.getResponsavel(), String.CASE_INSENSITIVE_ORDER);
                Comparator<Agendamento> porDataHora = Comparator.comparing(a -> {
                    LocalDateTime dt = a.getDataHora();
                    return dt == null ? LocalDateTime.MIN : dt;
                });

                switch (ordem) {
                    case "nome_az" -> agendamentos = agendamentos.stream().sorted(porNome).toList();
                    case "nome_za" -> agendamentos = agendamentos.stream().sorted(porNome.reversed()).toList();
                    case "mais_recente" -> agendamentos = agendamentos.stream().sorted(porDataHora.reversed()).toList();
                    case "mais_antigo" -> agendamentos = agendamentos.stream().sorted(porDataHora).toList();
                    default -> { }
                }
            }

            request.setAttribute("busca", busca);
            request.setAttribute("tipoFiltro", tipoFiltro);
            request.setAttribute("ordem", ordem);
            request.setAttribute("agendamentos", agendamentos);
            request.getRequestDispatcher("agendamentos.jsp").forward(request, response);
        } catch (PersistenciaException e) {
            throw new ServletException("Erro ao listar agendamentos: " + e.getMessage(), e);
        }
    }

    private Agendamento montarAgendamento(HttpServletRequest request, boolean atualizar) {
        Agendamento agendamento = new Agendamento();

        if (atualizar) {
            agendamento.setId(Long.valueOf(request.getParameter("id")));
        }

        agendamento.setTipo(request.getParameter("tipo"));
        agendamento.setResponsavel(request.getParameter("responsavel"));
        agendamento.setLocal(request.getParameter("local"));
        agendamento.setObservacoes(request.getParameter("observacoes"));

        String data = request.getParameter("data");
        if (data != null && !data.isBlank()) {
            try {
                agendamento.setData(LocalDate.parse(data));
            } catch (DateTimeParseException e) {
                agendamento.setData(null);
            }
        }

        String hora = request.getParameter("hora");
        if (hora != null && !hora.isBlank()) {
            try {
                agendamento.setHora(LocalTime.parse(hora));
            } catch (DateTimeParseException e) {
                agendamento.setHora(null);
            }
        }

        return agendamento;
    }

    private String parametroOuVazio(String valor) {
        return valor == null ? "" : valor;
    }
}
