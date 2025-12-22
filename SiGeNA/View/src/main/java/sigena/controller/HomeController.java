package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import sigena.model.common.exception.DataInvalidaException;
import sigena.model.common.exception.DatabaseException;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Evento;
import sigena.model.domain.Notificacao;
import sigena.model.domain.Tarefa;
import sigena.model.domain.Usuario;
import sigena.model.domain.util.Cargo;
import sigena.model.service.FuncionarioService;
import sigena.model.service.GestaoEventoService;
import sigena.model.service.GestaoNotificacaoService;
import sigena.model.service.GestaoProdutoService;
import sigena.model.service.GestaoTarefaService;

@WebServlet(name = "HomeController", urlPatterns = {"/HomeController"})
public class HomeController extends Controller {

    private GestaoNotificacaoService serviceN = new GestaoNotificacaoService();
    private GestaoEventoService serviceE = new GestaoEventoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);
        if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String acao = request.getParameter("acao");
        GestaoTarefaService service = new GestaoTarefaService();
        Usuario usuario = (Usuario) request.getSession().getAttribute("UsuarioLogado");

        LocalDate hoje = LocalDate.now();
        LocalDate ultimaExecucao = (LocalDate) request.getSession()
                .getAttribute("notificacaoExecutada");

        if (ultimaExecucao == null || !ultimaExecucao.equals(hoje)) {
            try {
                notificarEventos(usuario);
                request.getSession().setAttribute("notificacaoExecutada", hoje);
            } catch (PersistenciaException e) {
                e.printStackTrace();
            }
        }

        List<Notificacao> notificacoes = serviceN.listarPorUsuario(usuario.getId());

        request.setAttribute("notificacoes", notificacoes);

        try {
            if ("cadastrar".equals(acao)) {
                abrirFormularioCadastro(request, response);
                return;
            }

            if ("editar".equals(acao)) {
                long id = Long.parseLong(request.getParameter("id"));
                Tarefa tarefa = service.buscar(id);

                if (tarefa == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                String cpfLogado = getCpfUsuarioLogado(request);
                if (!tarefa.getCpfAutor().equals(cpfLogado)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                FuncionarioService funcionarioService = new FuncionarioService();
                request.setAttribute("funcionarios", funcionarioService.listar());
                request.setAttribute("tarefa", tarefa);

                request.getRequestDispatcher("editar-tarefa.jsp").forward(request, response);
                return;
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

        Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
        String cpf = (String) sessao.getAttribute("CpfLogado");

        List<Tarefa> tarefas
                = (cargo == Cargo.GERENTE)
                        ? service.listarTarefasDoDia()
                        : service.listarTarefasDoDiaPorCpf(cpf);

        request.setAttribute("tarefas", tarefas);
        request.setAttribute("isGerente", cargo == Cargo.GERENTE);

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {
            switch (acao) {
                case "inserir" ->
                    cadastrar(request, response);
                case "editar" ->
                    editar(request, response);
                case "excluir" ->
                    excluir(request, response);
                case "concluir" ->
                    concluir(request, response);
                case "lerNotificacao" ->
                    marcarComoLida(request, response);
                default ->
                    response.sendRedirect("HomeController");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void abrirFormularioCadastro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, DatabaseException {

        FuncionarioService funcionarioService = new FuncionarioService();
        request.setAttribute("funcionarios", funcionarioService.listar());
        request.getRequestDispatcher("cadastrar-tarefa.jsp").forward(request, response);
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException, DatabaseException, PersistenciaException {

        String nome = request.getParameter("nome");
        String texto = request.getParameter("texto");
        int idDestinatario = Integer.parseInt(request.getParameter("destinatario"));

        LocalDateTime dataPConclusao = LocalDateTime.parse(
                request.getParameter("data-conclusao"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        );

        String cpfAutor = getCpfUsuarioLogado(request);

        GestaoTarefaService service = new GestaoTarefaService();

        try {
            service.cadastrarTarefa(nome, texto, idDestinatario, dataPConclusao, cpfAutor);
        } catch (DataInvalidaException e) {
            request.setAttribute("msgErro", e.getMessage());
            abrirFormularioCadastro(request, response);
            return;
        }
        
        GestaoNotificacaoService not = new GestaoNotificacaoService();
        not.criarParaTodos("Nova tarefa cadastrada");
        response.sendRedirect("HomeController");
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException, DatabaseException {

        long id = Long.parseLong(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String texto = request.getParameter("texto");
        int idDestinatario = Integer.parseInt(request.getParameter("destinatario"));

        LocalDateTime dataPConclusao = LocalDateTime.parse(
                request.getParameter("data-conclusao"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        );

        String cpfLogado = getCpfUsuarioLogado(request);
        GestaoTarefaService service = new GestaoTarefaService();

        try {
            service.editar(id, nome, texto, idDestinatario, dataPConclusao, null, cpfLogado);
        } catch (DataInvalidaException e) {
            response.sendRedirect("HomeController?acao=editar&id=" + id);
            return;
        } catch (SecurityException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.sendRedirect("HomeController");
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        long id = Long.parseLong(request.getParameter("id"));
        String cpf = getCpfUsuarioLogado(request);

        GestaoTarefaService service = new GestaoTarefaService();
        Tarefa tarefa = service.buscar(id);

        if (tarefa == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            service.excluir(tarefa, cpf);
        } catch (SecurityException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.sendRedirect("HomeController");
    }

    private void concluir(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        long id = Long.parseLong(request.getParameter("id"));
        String cpf = getCpfUsuarioLogado(request);

        String statusParam = request.getParameter("status");
        boolean novoStatus = (statusParam != null) ? Boolean.parseBoolean(statusParam) : true;

        if (novoStatus == true) {
            GestaoTarefaService service = new GestaoTarefaService();
            service.editarConcluida(id, true, cpf);
        }

        response.sendRedirect("HomeController");
    }

    public void notificarEventos(Usuario usuario) throws PersistenciaException, ServletException, IOException {
        LocalDateTime amanha = LocalDate.now().plusDays(1).atStartOfDay();

        List<Evento> eventos = serviceE.listarEventos(amanha, amanha.with(LocalTime.MAX));
        
        for (Evento e : eventos) {
            if (!serviceN.eventoJaNotificado(usuario.getId(), "Amanhã você tem o evento " + e.getTitulo() + "", amanha)) {
                Notificacao n = new Notificacao(usuario.getId(), "Amanhã você tem o evento " + e.getTitulo() + "");
                serviceN.salvar(n);
            }
        }
    }

    private void marcarComoLida(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Notificacao n = serviceN.buscarPorId(id);
        serviceN.marcarComoLida(n);
        response.sendRedirect("HomeController");
    }

}
