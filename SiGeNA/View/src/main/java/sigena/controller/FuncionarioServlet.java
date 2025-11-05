package sigena.controller;

import sigena.model.dao.FuncionarioDAO;
import sigena.model.domain.Funcionario;
import sigena.model.domain.Cargo;
import sigena.model.domain.EstadoFuncionario;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class FuncionarioServlet extends HttpServlet {

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("cadastrar".equals(acao)) {
            Funcionario f = new Funcionario();
            f.setNome(request.getParameter("nome"));
            f.setCargo(Cargo.valueOf(request.getParameter("cargo")));
            f.setAreaAtuacao(request.getParameter("area"));
            f.setObservacoes(request.getParameter("observacoes"));
            funcionarioDAO.salvar(f);

            response.sendRedirect("funcionario.jsp");
        }
        else if ("deletar".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            funcionarioDAO.remover(id);
            response.sendRedirect("funcionario.jsp");
        }
    }
}
