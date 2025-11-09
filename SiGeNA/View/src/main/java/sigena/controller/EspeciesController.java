package sigena.controller;

import sigena.model.dao.EspecieDAO;
import sigena.model.domain.Especie;
import sigena.model.common.exception.PersistenciaException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/EspeciesController")
public class EspeciesController extends HttpServlet {

    private EspecieDAO dao = new EspecieDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        try {
            if ("excluir".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try {
                    dao.excluir(id);
                } catch (PersistenciaException e) {
                    request.setAttribute("erro", "Não é possível excluir: há animais vinculados a essa espécie.");
                }
            }
            if ("ver".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Especie especie = dao.buscarPorId(id);
                request.setAttribute("especie", especie);
                request.getRequestDispatcher("ver-especie.jsp").forward(request, response);
                return;
            }
            if ("editar".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Especie especie = dao.buscarPorId(id);
                request.setAttribute("especie", especie);
                request.getRequestDispatcher("editar-especie.jsp").forward(request, response);
                return;
            }

            List<Especie> lista = dao.listar();
            request.setAttribute("lista", lista);
            request.getRequestDispatcher("especies.jsp").forward(request, response);
        } catch (PersistenciaException e) {
            throw new ServletException("Erro no banco de dados: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        try {
            if (acao == null) {
                Especie e = new Especie();
                e.setNome(request.getParameter("nome"));
                e.setClasse(request.getParameter("classe"));
                e.setHabitat(request.getParameter("habitat"));
                e.setAlimentacao(request.getParameter("alimentacao"));
                e.setPredador(request.getParameter("predador") != null);
                e.setObservacoes(request.getParameter("observacoes"));

                if (e.getHabitat() == null || e.getHabitat().isEmpty()
                        || e.getAlimentacao() == null || e.getAlimentacao().isEmpty()) {
                    request.setAttribute("erro", "Habitat e Alimentação são obrigatórios.");
                    request.getRequestDispatcher("cadastrar-especie.jsp").forward(request, response);
                    return;
                }

                dao.inserir(e);
                response.sendRedirect("EspeciesController");
                return;
            }
            if ("atualizar".equals(acao)) {
                Especie e = new Especie();
                e.setId(Integer.parseInt(request.getParameter("id")));
                e.setNome(request.getParameter("nome"));
                e.setClasse(request.getParameter("classe"));
                e.setHabitat(request.getParameter("habitat"));
                e.setAlimentacao(request.getParameter("alimentacao"));
                e.setPredador(request.getParameter("predador") != null);
                e.setObservacoes(request.getParameter("observacoes"));

                dao.atualizar(e);
                response.sendRedirect("EspeciesController?acao=ver&id=" + e.getId());
            }
        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());

            if ("atualizar".equals(acao)) {
                request.getRequestDispatcher("editar-especie.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("cadastrar-especie.jsp").forward(request, response);
            }
        }
    }
}
