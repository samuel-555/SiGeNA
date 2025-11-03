package sigena.controller;

import java.io.IOException;
import sigena.model.dao.UsuarioDAO;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cpf = request.getParameter("cpf").trim();
        String senha = request.getParameter("senha").trim();
         System.out.println("Tentando login - CPF: '" + cpf + "' Senha: '" + senha + "'");
        UsuarioDAO dao = new UsuarioDAO();
        if(dao.autenticar(cpf, senha)) {
            HttpSession session = request.getSession();
            session.setAttribute("CpfLogado", cpf);
            response.sendRedirect("home.jsp");
         
        } 
        else {
            request.setAttribute("erro", "Cpf ou senha inválidos!");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }
            
    }
}
