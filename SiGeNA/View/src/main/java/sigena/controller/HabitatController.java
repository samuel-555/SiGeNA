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
import java.util.ArrayList;
import java.util.List;
import sigena.model.domain.Habitat;
import sigena.model.service.GestaoHabitatService;


@WebServlet(name = "HabitatController", urlPatterns = {"/HabitatController"})
public class HabitatController extends HttpServlet {

    
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

        GestaoHabitatService service = new GestaoHabitatService();

        String acao = request.getParameter("acao");

        if ("editar".equals(acao)) {
            String nome = request.getParameter("nome"); 
            Habitat habitat = service.buscar(nome);

            request.setAttribute("habitat", habitat);
            request.getRequestDispatcher("editar-habitat.jsp").forward(request, response);
            return;
        }
        
        List<Habitat> habitats = service.listarHabitats();
        request.setAttribute("habitats", habitats);
        request.getRequestDispatcher("habitats.jsp").forward(request, response);
}


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        switch(acao){
            case "inserir":
                cadastrar(request, response);
                break;
            case "editar":
                editar(request, response);
                break;
            case "excluir":
                excluir(request, response);
                break;
        }
    }
    
    public void cadastrar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String nome = request.getParameter("nome");
        String tipo = request.getParameter("tipo");
        
        
        int tamanho = Integer.parseInt(request.getParameter("tamanho"));
        
        boolean manutencao = request.getParameter("manutencao") != null;

        GestaoHabitatService service = new GestaoHabitatService();
            
        try{
            service.cadastrarHabitat(tipo, nome,tamanho, manutencao);
            response.sendRedirect("HabitatController");
        }
        catch (RuntimeException e){
            request.setAttribute("msgErro", "Já existe um habitat com este nome.");
            
            request.setAttribute("tipo", tipo);
            request.setAttribute("tamanho", tamanho);
            request.setAttribute("manutencao", manutencao);
            
            request.getRequestDispatcher("cadastrar-habitat.jsp").forward(request, response);
        }
    }
   
   public void editar(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String nome = request.getParameter("nome");
        String tipo = request.getParameter("tipo");
        int tamanho = Integer.parseInt(request.getParameter("tamanho"));
        boolean manutencao = request.getParameter("manutencao") != null;

        String nomeAntigo = request.getParameter("nomeAntigo");

        GestaoHabitatService service = new GestaoHabitatService();
        service.editar(nomeAntigo, nome, tipo, tamanho, manutencao);

        response.sendRedirect("HabitatController");
    }

    
    public void excluir(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nome = request.getParameter("nome");

        GestaoHabitatService service = new GestaoHabitatService();
        Habitat habitat = new Habitat("","",0,false);
        habitat.setNome(nome);

        service.excluir(habitat);

        response.sendRedirect("HabitatController");
}
            
}
