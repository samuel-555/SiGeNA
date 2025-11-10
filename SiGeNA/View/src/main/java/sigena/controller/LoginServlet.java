package sigena.controller;

import java.io.IOException;
import sigena.model.service.GestaoUsuarioService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Usuario;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private GestaoUsuarioService dao = new GestaoUsuarioService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cpf = request.getParameter("cpf");
        String senha = request.getParameter("senha");
        Usuario usuario = new Usuario(cpf, senha);

        try {
            if (dao.autenticar(cpf, senha)) {
                HttpSession session = request.getSession();
                session.setAttribute("CpfLogado", cpf);
                session.setAttribute("UsuarioLogado", usuario);
                response.sendRedirect("home.jsp");
            } else {
                request.setAttribute("erro", "CPF ou senha inválidos!");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (PersistenciaException e) {
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
