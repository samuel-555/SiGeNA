package sigena.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import sigena.model.domain.Historico;
import sigena.model.service.GestaoHistoricoService;

@WebServlet(name = "HistoricoController", urlPatterns = {"/HistoricoController"})
public class HistoricoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        if (acao == null) acao = "buscar";

        switch(acao){
            case "listar":
                listar(request, response);
                break;
            case "buscar":
                buscar(request, response);
                break;
            default:
                buscar(request, response);
        }
    }

    public void listar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String cpfFuncionario = request.getParameter("funcionario");
        GestaoHistoricoService service = new GestaoHistoricoService();
        List<Historico> historico = service.listarPorFuncionario(cpfFuncionario);
        request.setAttribute("historico", historico);
        request.getRequestDispatcher("historico.jsp").forward(request, response);
    }

    public void buscar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String termo = request.getParameter("q");
        GestaoHistoricoService service = new GestaoHistoricoService();
        Map<String, List<Historico>> historicoMap;

        if (termo == null || termo.isBlank()) {
            historicoMap = service.listarAgrupadoPorFuncionario();
        } else {
            historicoMap = service.buscarAgrupado(termo);
        }
        
        request.setAttribute("historicoMap", historicoMap);
        request.getRequestDispatcher("historico.jsp").forward(request, response);
    }
}