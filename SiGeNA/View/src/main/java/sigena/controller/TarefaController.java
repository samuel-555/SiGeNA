
package sigena.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import sigena.model.common.exception.DataInvalidaException;
import sigena.model.common.exception.DatabaseException;
import sigena.model.domain.util.Cargo;
import sigena.model.domain.Tarefa;
import sigena.model.service.FuncionarioService;
import sigena.model.service.GestaoTarefaService;

@WebServlet(name = "TarefaController", urlPatterns = {"/TarefaController"})
public class TarefaController extends Controller {

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
                case "inserir":
                    cadastrar(request, response);
                    break;
                case "editar" :
                    editar(request, response);
                    break;
                case "excluir":
                    excluir(request, response);
                    break;
                case "concluir":
                    concluir(request, response);
                    break;
                default:
                    response.sendRedirect("TarefaController");
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
            throws IOException, ServletException, SQLException, DatabaseException {

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

        response.sendRedirect("TarefaController");
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
            response.sendRedirect("TarefaController?acao=editar&id=" + id);
            return;
        } catch (SecurityException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        catch (IllegalStateException e) {
            response.sendRedirect("TarefaController");
            return;
}

        response.sendRedirect("TarefaController");
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
        catch (IllegalStateException e) {
            response.sendRedirect("TarefaController");
            return;
        }

        response.sendRedirect("TarefaController");
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
        
        response.sendRedirect("TarefaController");
    }
}
