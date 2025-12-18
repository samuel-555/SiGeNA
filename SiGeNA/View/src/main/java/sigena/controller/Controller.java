package sigena.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public abstract class Controller extends HttpServlet {

    protected String getCpfUsuarioLogado(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new IllegalStateException("Sessão não encontrada");
        }

        String cpf = (String) session.getAttribute("CpfLogado");

        if (cpf == null) {
            throw new IllegalStateException("Usuário não logado");
        }

        return cpf;
    }

        
    protected void forwardErro(
            HttpServletRequest request,
            HttpServletResponse response,
            String mensagem)
            throws ServletException, IOException {

        request.setAttribute("msgErro", mensagem);
        request.getRequestDispatcher("/erro.jsp")
               .forward(request, response);
    }

    protected void redirect(
            HttpServletRequest request,
            HttpServletResponse response,
            String url)
            throws IOException {

        response.sendRedirect(
            request.getContextPath() + "/" + url
        );
    }
}
