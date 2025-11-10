package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import sigena.model.domain.Animal;
import sigena.model.domain.Tratamento;
import sigena.model.domain.Usuario;
import sigena.model.domain.util.StatusTratamento;
import sigena.model.domain.util.TipoTratamento;
import java.time.format.DateTimeFormatter;
import sigena.model.dao.TratamentoDAO;
import sigena.model.service.GestaoAnimalService;

@WebServlet(name = "TratamentosServlet", urlPatterns = {"/TratamentosServlet"})
public class TratamentosServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TratamentosServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TratamentosServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if ("salvar".equalsIgnoreCase(acao)) {
            try {
                cadastrar(request);
                request.setAttribute("mensagemSucesso", "Tratamento cadastrado com sucesso!");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("mensagemErro", "Erro ao cadastrar tratamento: " + e.getMessage());
            }

            // Encaminha de volta para a página JSP
            request.getRequestDispatcher("tratamentos.jsp").forward(request, response);
        } else {
            request.setAttribute("mensagemErro", "Ação inválida");
            request.getRequestDispatcher("tratamentos.jsp").forward(request, response);
        }
    }

    public void cadastrar(HttpServletRequest request) throws Exception {
        GestaoAnimalService service = new GestaoAnimalService();
        Long animalId = Long.valueOf(request.getParameter("animal"));
        Animal animal = service.buscarAnimal(animalId);
        if (animal == null) {
            throw new Exception("Animal não encontrado.");
        }
        Usuario usuario = (Usuario) request.getSession().getAttribute("UsuarioLogado");
        if (usuario == null) {
            throw new Exception("Usuário não autenticado.");
        }
        String diagnostico = request.getParameter("diagnostico");
        String medicacao = request.getParameter("medicacao");
        //int frequencia = Integer.valueOf(request.getParameter("frequencia"));
        int frequencia = 0;
        try {
            frequencia = Integer.parseInt(request.getParameter("frequencia"));
        } catch (NumberFormatException e) {
            frequencia = 0; // ou trate de forma adequada
        }

        String obs = request.getParameter("observacoes");
        TipoTratamento tipo = TipoTratamento.valueOf(request.getParameter("tipoTratamento").toUpperCase());
        StatusTratamento status = StatusTratamento.EM_ANDAMENTO;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dataFinal = LocalDateTime.parse(request.getParameter("data"), formatter);
        Tratamento tratamento = new Tratamento(animal, usuario, diagnostico, medicacao, frequencia, obs, tipo, status, dataFinal);

        TratamentoDAO dao = new TratamentoDAO();
        dao.cadastrar(animal, usuario, tratamento);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
