package sigena.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import sigena.model.common.exception.PersistenciaException;
import sigena.model.dao.AnimalDAO;
import sigena.model.dao.PlanoAlimentarDAO;
import sigena.model.domain.Animal;
import sigena.model.domain.PlanoAlimentar;
import sigena.model.domain.ItemPlanoAlimentar;

@WebServlet("/PlanosAlimentaresController")
public class PlanosAlimentaresController extends HttpServlet {

    private final PlanoAlimentarDAO dao = new PlanoAlimentarDAO();
    private final AnimalDAO animalDAO = new AnimalDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        try {
            if ("excluir".equals(acao)) {
                Long id = Long.valueOf(request.getParameter("id"));
                dao.excluir(id);
            } else if ("ver".equals(acao)) {
                Long id = Long.valueOf(request.getParameter("id"));
                PlanoAlimentar plano = dao.buscarPorId(id);
                request.setAttribute("plano", plano);
                request.getRequestDispatcher("ver-plano-alimentar.jsp").forward(request, response);
                return;
            } else if ("editar".equals(acao)) {
                Long id = Long.valueOf(request.getParameter("id"));
                PlanoAlimentar plano = dao.buscarPorId(id);
                List<Animal> animais = animalDAO.listar();
                request.setAttribute("animais", animais);
                request.setAttribute("plano", plano);
                request.getRequestDispatcher("editar-plano-alimentar.jsp").forward(request, response);
                return;
            } else if ("cadastrar".equals(acao)) {
                List<Animal> animais = animalDAO.listar();
                request.setAttribute("animais", animais);
                request.getRequestDispatcher("cadastrar-plano-alimentar.jsp").forward(request, response);
                return;
            }

            List<PlanoAlimentar> lista = dao.listar();
            request.setAttribute("lista", lista);
            request.getRequestDispatcher("planos-alimentares.jsp").forward(request, response);
        } catch (PersistenciaException e) {
            throw new ServletException("Erro no banco de dados: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        try {
            if (acao == null || acao.isBlank() || "salvar".equals(acao)) {
                Long animalId = Long.valueOf(request.getParameter("animal_id"));
                String[] alimentos = request.getParameterValues("alimento");
                String[] gramaturas = request.getParameterValues("gramatura");
                String[] vezesArr = request.getParameterValues("vezes_por_dia");

                Animal animal = animalDAO.buscarPorId(animalId);
                PlanoAlimentar plano = new PlanoAlimentar(animal);
                if (alimentos != null) {
                    for (int i = 0; i < alimentos.length; i++) {
                        String a = alimentos[i];
                        if (a == null || a.isBlank()) continue;
                        Double g = (gramaturas != null && gramaturas.length > i && gramaturas[i] != null && !gramaturas[i].isBlank()) ? Double.valueOf(gramaturas[i]) : 0.0;
                        Integer v = (vezesArr != null && vezesArr.length > i && vezesArr[i] != null && !vezesArr[i].isBlank()) ? Integer.valueOf(vezesArr[i]) : 1;
                        plano.addItem(new ItemPlanoAlimentar(a, g, v));
                    }
                }
                dao.inserir(plano);
                response.sendRedirect("PlanosAlimentaresController");
                return;
            }

            if ("atualizar".equals(acao)) {
                Long id = Long.valueOf(request.getParameter("id"));
                Long animalId = Long.valueOf(request.getParameter("animal_id"));
                String[] alimentos = request.getParameterValues("alimento");
                String[] gramaturas = request.getParameterValues("gramatura");
                String[] vezesArr = request.getParameterValues("vezes_por_dia");

                Animal animal = animalDAO.buscarPorId(animalId);
                PlanoAlimentar plano = new PlanoAlimentar(id, animal);
                if (alimentos != null) {
                    for (int i = 0; i < alimentos.length; i++) {
                        String a = alimentos[i];
                        if (a == null || a.isBlank()) continue;
                        Double g = (gramaturas != null && gramaturas.length > i && gramaturas[i] != null && !gramaturas[i].isBlank()) ? Double.valueOf(gramaturas[i]) : 0.0;
                        Integer v = (vezesArr != null && vezesArr.length > i && vezesArr[i] != null && !vezesArr[i].isBlank()) ? Integer.valueOf(vezesArr[i]) : 1;
                        plano.addItem(new ItemPlanoAlimentar(a, g, v));
                    }
                }
                dao.atualizar(plano);
                response.sendRedirect("PlanosAlimentaresController?acao=ver&id=" + id);
                return;
            }
        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            if ("atualizar".equals(acao)) {
                request.getRequestDispatcher("editar-plano-alimentar.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("cadastrar-plano-alimentar.jsp").forward(request, response);
            }
        }
    }
}
