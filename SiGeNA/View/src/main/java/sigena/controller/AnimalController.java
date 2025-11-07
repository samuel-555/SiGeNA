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
import sigena.model.domain.Animal;
import sigena.model.service.GestaoAnimalService;

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
            GestaoAnimalService service = new GestaoAnimalService();
            List<Animal> animais = new ArrayList<>();
            
            animais = service.listarAnimais();
            
            request.setAttribute("animais", animais);
            
            response.sendRedirect("animais.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        
        
        if(acao.equals("salvar")) {
            cadastrar(request, response);
            response.sendRedirect("animais.jsp");
        }
    }
    
    private void cadastrar(HttpServletRequest request, HttpServletResponse response) {
        String nome = request.getParameter("nome");
        String sexo = request.getParameter("sexo");
        String dataDeNascimento = request.getParameter("dataDeNascimento");
        Double peso = Double.valueOf(request.getParameter("peso"));
        boolean hostil = request.getParameter("hostil") != null;
            
        Animal novoAnimal = new Animal(nome, sexo, dataDeNascimento, peso, hostil);
        GestaoAnimalService service = new GestaoAnimalService();
            
        service.cadastrarAnimal(novoAnimal);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}