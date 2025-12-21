package sigena.controller;

import java.io.IOException;
import sigena.model.dao.UsuarioDAO;
import sigena.model.domain.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import sigena.model.common.exception.PersistenciaException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cpf = request.getParameter("cpf");
        if (cpf != null) {
            cpf = cpf.trim();
        }
        String senha = request.getParameter("senha");
        Usuario usuario1 = new Usuario(cpf, senha);

        try {
            if (cpf == null || !cpf.matches("\\d{11}")) {
                request.setAttribute("erro", "O CPF deve conter exatamente 11 dígitos numéricos.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }
            var usuario = dao.autenticar(cpf, senha);
            if (usuario != null) {
                String nomeParaSessao = usuario.getCpf();
                try {
                    String nomeFuncionario = dao.buscarNomePorCpf(usuario.getCpf());
                    if (nomeFuncionario != null && !nomeFuncionario.isBlank()) {
                        nomeParaSessao = nomeFuncionario;
                    }
                } catch (PersistenciaException ignored) {
                    nomeParaSessao = usuario.getCpf();
                }

                HttpSession session = request.getSession();
                session.setAttribute("CpfLogado", usuario.getCpf());
                session.setAttribute("NomeLogado", nomeParaSessao);
                session.setAttribute("cargoUsuario", usuario.getCargo());
                session.setAttribute("UsuarioLogado", usuario1);
                response.sendRedirect("TarefaController");
            } else {
                request.setAttribute("erro", "CPF ou senha inválidos!");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (sigena.model.common.exception.PersistenciaException e) {
            request.setAttribute("erro", "Erro ao autenticar: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
