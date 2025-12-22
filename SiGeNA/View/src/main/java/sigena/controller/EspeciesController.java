package sigena.controller;

import sigena.model.service.GestaoEspeciesService;
import sigena.model.domain.Especie;
import sigena.model.common.exception.PersistenciaException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/EspeciesController")
public class EspeciesController extends HttpServlet {

    private GestaoEspeciesService service = new GestaoEspeciesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        try {
            if ("excluir".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                try {
                    service.excluir(id);
                } catch (PersistenciaException e) {
                    request.setAttribute("erro", "Não é possível excluir: há animais vinculados a essa espécie.");
                }
            }
            if ("ver".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Especie especie = service.buscarPorId(id);
                request.setAttribute("especie", especie);
                request.getRequestDispatcher("ver-especie.jsp").forward(request, response);
                return;
            }
            if ("editar".equals(acao)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Especie especie = service.buscarPorId(id);
                request.setAttribute("especie", especie);
                request.getRequestDispatcher("editar-especie.jsp").forward(request, response);
                return;
            }

            List<Especie> lista = service.listar();
            String nomeBusca = request.getParameter("busca");
            String predadorFiltro = request.getParameter("predador");
            String ordem = request.getParameter("ordem");

            if ((nomeBusca != null && !nomeBusca.isBlank()) || (predadorFiltro != null && !predadorFiltro.isBlank())) {
                String termo = nomeBusca == null ? "" : nomeBusca.toLowerCase();
                String filtroPredador = predadorFiltro == null ? "" : predadorFiltro.toLowerCase();

                lista = lista.stream()
                        .filter(e -> termo.isBlank() || (e.getNome() != null && e.getNome().toLowerCase().contains(termo)))
                        .filter(e -> {
                            if (filtroPredador.isBlank()) return true;
                            if ("sim".equals(filtroPredador)) return e.isPredador();
                            if ("nao".equals(filtroPredador)) return !e.isPredador();
                            return true;
                        })
                        .toList();
            }

            if (ordem != null && !ordem.isBlank()) {
                switch (ordem) {
                    case "nome_az" -> lista = lista.stream()
                            .sorted((a, b) -> {
                                String nomeA = a.getNome() == null ? "" : a.getNome();
                                String nomeB = b.getNome() == null ? "" : b.getNome();
                                return nomeA.compareToIgnoreCase(nomeB);
                            })
                            .toList();
                    case "nome_za" -> lista = lista.stream()
                            .sorted((a, b) -> {
                                String nomeA = a.getNome() == null ? "" : a.getNome();
                                String nomeB = b.getNome() == null ? "" : b.getNome();
                                return nomeB.compareToIgnoreCase(nomeA);
                            })
                            .toList();
                    case "mais_recente" -> lista = lista.stream()
                            .sorted((a, b) -> Integer.compare(b.getId(), a.getId()))
                            .toList();
                    case "mais_antigo" -> lista = lista.stream()
                            .sorted((a, b) -> Integer.compare(a.getId(), b.getId()))
                            .toList();
                    default -> {
                    }
                }
            }
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

                service.inserir(e);
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

                service.atualizar(e);
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
