package sigena.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

import sigena.model.domain.Cargo;
import sigena.model.domain.Funcionario;
import sigena.model.domain.Turno;
import sigena.model.domain.EstadoFuncionario;
import sigena.model.service.FuncionarioService;
import sigena.model.common.exception.BusinessException;
import sigena.model.common.exception.DatabaseException;
import sigena.model.common.exception.ValidationException;

@WebServlet(name = "FuncionarioServlet", urlPatterns = {"/FuncionarioServlet"})
public class FuncionarioServlet extends HttpServlet {

    private final FuncionarioService service = new FuncionarioService();

    private boolean isGerente(HttpSession s) {
        Object c = (s != null) ? s.getAttribute("cargoUsuario") : null;
        return (c instanceof Cargo) && ((Cargo) c) == Cargo.GERENTE;
    }

    private boolean exigirGerente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (!isGerente(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso restrito ao gerente.");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!exigirGerente(req, resp)) return;

        String acao = req.getParameter("acao");

        if ("editar".equals(acao)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                Funcionario f = service.buscarPorId(id);
                req.setAttribute("funcionario", f);
                req.getRequestDispatcher("cadastrar-funcionario.jsp").forward(req, resp);
                return;
            } catch (Exception e) {
                req.setAttribute("erro", e.getMessage());
                listar(req, resp);
            }
        } else {
            listar(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("funcionarios", service.listar());
        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
        }
        req.getRequestDispatcher("funcionario.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!exigirGerente(req, resp)) return;

        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");

        try {
            if ("salvar".equals(acao)) {
                Funcionario f = montar(req);
                service.salvar(f);
                resp.sendRedirect(req.getContextPath() + "/FuncionarioServlet");

            } else if ("atualizar".equals(acao)) {
                Funcionario f = montar(req);
                f.setId(Integer.parseInt(req.getParameter("id")));
                service.atualizar(f);
                resp.sendRedirect(req.getContextPath() + "/FuncionarioServlet");

            } else if ("deletar".equals(acao)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String setor = req.getParameter("setor");
                String turno = req.getParameter("turno");
                service.deletar(id, setor, turno);
                resp.sendRedirect(req.getContextPath() + "/FuncionarioServlet");
            }

        } catch (ValidationException ve) {
            req.setAttribute("erro", "Erro de validação: " + ve.getMessage());
            listar(req, resp);
        } catch (BusinessException be) {
            req.setAttribute("erro", be.getMessage());
            listar(req, resp);
        } catch (DatabaseException de) {
            req.setAttribute("erro", "Falha no banco de dados: " + de.getMessage());
            listar(req, resp);
        } catch (NumberFormatException | SQLException e) {
            req.setAttribute("erro", "Erro inesperado: " + e.getMessage());
            listar(req, resp);
        }
    }

    private Funcionario montar(HttpServletRequest req) {
        Funcionario f = new Funcionario();
        f.setNome(n(req.getParameter("nome")));
        f.setCpf(n(req.getParameter("cpf")));
        f.setSenha(n(req.getParameter("senha")));
        f.setCargo(Cargo.valueOf(req.getParameter("cargo")));
        f.setAreaAtuacao(n(req.getParameter("area")));
        f.setTurno(Turno.valueOf(req.getParameter("turno")));
        f.setEstado(EstadoFuncionario.valueOf(req.getParameter("estado")));
        f.setObservacoes(n(req.getParameter("observacoes")));
        return f;
    }

    private String n(String s) {
        return s == null ? "" : s.trim();
    }
}