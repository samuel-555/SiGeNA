/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import sigena.model.domain.Historico;
import sigena.model.domain.TipoHistorico;
import sigena.model.service.GestaoHistoricoService;


@WebServlet(name = "HistoricoController", urlPatterns = {"/HistoricoController"})
public class HistoricoController extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet HabitatController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HabitatController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        GestaoHistoricoService service = new GestaoHistoricoService();

        String acao = request.getParameter("acao");
        if (acao == null) {
            response.sendRedirect("historico.jsp");
            return;
        }

        switch(acao){
            case "listar":
                listar(request, response);
                break;
            case "buscar":
                buscar(request, response);
                break;
            default:
                response.sendRedirect("historico.jsp");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        switch(acao){
            case "listar":
                listar(request, response);
                break;
            case "buscar":
                buscar(request, response);
                break;
        }
    }
    
   
   public void listar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
       
        int idFuncionario = Integer.parseInt(request.getParameter("funcionario"));
        GestaoHistoricoService service = new GestaoHistoricoService();

        List<Historico> historico = service.listarPorFuncionario(idFuncionario);
        request.setAttribute("historico", historico);

        request.getRequestDispatcher("historico.jsp").forward(request, response);
    }

    
    public void buscar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
       
        String termo = request.getParameter("q");

        GestaoHistoricoService service = new GestaoHistoricoService();
        List<Historico> historico;
      
        TipoHistorico tipo = TipoHistorico.from(termo);
        if(tipo != null)
            historico = service.buscarPorTipo(tipo);
        else
            historico = service.buscarPorFuncionario(termo);
        
        request.setAttribute("historico", historico);
        request.getRequestDispatcher("historico.jsp").forward(request, response);
}
            
}
