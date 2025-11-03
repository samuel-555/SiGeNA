package sigena.controller;

import sigena.model.dao.FuncionarioDAO;
import sigena.model.domain.Funcionario;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/FuncionarioServlet")
public class FuncionarioServlet extends HttpServlet {

    private Connection getConnection() throws SQLException {
        // banco de dados:)
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/sigena", "root", "senha");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection conn = getConnection()) {
            FuncionarioDAO dao = new FuncionarioDAO(conn);
            List<Funcionario> lista = dao.listar();
            request.setAttribute("funcionarios", lista);
            RequestDispatcher dispatcher = request.getRequestDispatcher("funcionarioList.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String cargo = request.getParameter("cargo");

        Funcionario funcionario = new Funcionario(nome, email, senha, cargo);

        try (Connection conn = getConnection()) {
            FuncionarioDAO dao = new FuncionarioDAO(conn);
            dao.inserir(funcionario);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        response.sendRedirect("FuncionarioServlet");
    }
}
