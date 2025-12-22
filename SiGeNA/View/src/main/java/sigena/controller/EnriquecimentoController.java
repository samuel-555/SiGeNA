package sigena.controller;

import sigena.model.domain.Enriquecimento;
import sigena.model.service.GestaoEnriquecimentoService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/enriquecimento")
public class EnriquecimentoController extends HttpServlet {

    private final GestaoEnriquecimentoService service = new GestaoEnriquecimentoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        try {
            if ("cadastrar".equals(action)) {
                req.setAttribute("habitats", service.listarHabitatsDisponiveis());
                req.getRequestDispatcher("/cadastrar-enriquecimento.jsp").forward(req, resp);
                return;
            }
            
            if ("ver".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Enriquecimento e = service.buscarPorId(id);
                req.setAttribute("enriquecimento", e);
                req.getRequestDispatcher("/ver-enriquecimento.jsp").forward(req, resp);
                return;
            }

            if ("editar".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Enriquecimento e = service.buscarPorId(id);
                req.setAttribute("enriquecimento", e);
                req.setAttribute("habitats", service.listarHabitatsDisponiveis());
                req.getRequestDispatcher("/editar-enriquecimento.jsp").forward(req, resp);
                return;
            }

            if ("deletar".equals(action)) {
                String idStr = req.getParameter("id");
                if (idStr != null) {
                    service.remover(Integer.parseInt(idStr));
                }
                resp.sendRedirect(req.getContextPath() + "/enriquecimento");
                return;
            }

            List<Enriquecimento> lista = service.listarTodos();
            String busca = req.getParameter("busca");
            String habitat = req.getParameter("habitat");
            String ordem = req.getParameter("ordem");

            if ((busca != null && !busca.isBlank()) || (habitat != null && !habitat.isBlank())) {
                String termo = busca == null ? "" : busca.toLowerCase();
                String habitatFiltro = habitat == null ? "" : habitat.toLowerCase();

                lista = lista.stream()
                        .filter(e -> termo.isBlank() || (e.getNome() != null && e.getNome().toLowerCase().contains(termo)))
                        .filter(e -> {
                            if (habitatFiltro.isBlank()) return true;
                            List<String> habitatsEnriq = e.getHabitats();
                            if (habitatsEnriq == null) return false;
                            return habitatsEnriq.stream().anyMatch(h -> h != null && h.toLowerCase().equals(habitatFiltro));
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
                            .sorted(Comparator.comparing(Enriquecimento::getDataCriacao,
                                    Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                            .toList();
                    case "mais_antigo" -> lista = lista.stream()
                            .sorted(Comparator.comparing(Enriquecimento::getDataCriacao,
                                    Comparator.nullsLast(Comparator.naturalOrder())))
                            .toList();
                    default -> {
                    }
                }
            }

            req.setAttribute("listaEnriquecimentos", lista);
            req.setAttribute("habitats", service.listarHabitatsDisponiveis());
            req.getRequestDispatcher("/enriquecimento.jsp").forward(req, resp);

        } catch (SQLException ex) {
            throw new ServletException("Erro ao acessar enriquecimentos: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if ("update".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                Enriquecimento e = service.buscarPorId(id);

                e.setNome(req.getParameter("nome"));
                e.setTipo(req.getParameter("tipo"));
                e.setEspecieDestinada(req.getParameter("especie"));
                e.setFrequencia(req.getParameter("frequencia"));
                e.setObservacoes(req.getParameter("observacoes"));

                String[] habitatsArr = req.getParameterValues("habitats");
                List<String> habitats = habitatsArr == null ? List.of() :
                        Arrays.stream(habitatsArr).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

                service.atualizarEnriquecimento(e, habitats);
                resp.sendRedirect(req.getContextPath() + "/enriquecimento?action=ver&id=" + id);
                return;

            } catch (SQLException ex) {
                throw new ServletException("Erro ao atualizar enriquecimento: " + ex.getMessage(), ex);
            }
        }

        String nome = req.getParameter("nome");
        String tipo = req.getParameter("tipo");
        String especie = req.getParameter("especie");
        String frequencia = req.getParameter("frequencia");
        String observacoes = req.getParameter("observacoes");
        String[] habitatsArr = req.getParameterValues("habitats");

        List<String> habitats = habitatsArr == null ? List.of() : Arrays.stream(habitatsArr)
                .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        Enriquecimento e = new Enriquecimento();
        e.setNome(nome);
        e.setTipo(tipo);
        e.setEspecieDestinada(especie);
        e.setFrequencia(frequencia);
        e.setObservacoes(observacoes);

        try {
            service.criarEnriquecimento(e, habitats);
            resp.sendRedirect(req.getContextPath() + "/enriquecimento");

        } catch (IllegalArgumentException ia) {
            try {
                req.setAttribute("erro", ia.getMessage());
                req.setAttribute("habitats", service.listarHabitatsDisponiveis());
                req.getRequestDispatcher("/cadastrar-enriquecimento.jsp").forward(req, resp);
            } catch (SQLException ex) {
                throw new ServletException("Erro ao recarregar formulário: " + ex.getMessage(), ex);
            }
        } catch (SQLException ex) {
            throw new ServletException("Erro ao criar enriquecimento: " + ex.getMessage(), ex);
        }
    }
}
