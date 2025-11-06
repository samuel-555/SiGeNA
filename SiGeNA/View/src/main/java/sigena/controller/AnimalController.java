package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sigena.model.domain.Animal;

@WebServlet(name = "AnimalController", urlPatterns = {"/AnimalController"})
public class AnimalController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AnimalController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AnimalController at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
        
        String acao = request.getParameter("acao");
        
        if(acao.equals("salvar")) {
            String nome = request.getParameter("nome");
            String especie = request.getParameter("especie");
            Integer idade = null;
            if(request.getParameter("idade") != null)
                idade = Integer.parseInt(request.getParameter("idade"));
            
            Double peso = null;
            if(request.getParameter("peso") != null)
                peso = Double.parseDouble(request.getParameter("peso"));
            
            String habitat = request.getParameter("habitat");
            
            Animal novoAnimal = new Animal(nome, especie, idade, peso, habitat);
            
            
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}