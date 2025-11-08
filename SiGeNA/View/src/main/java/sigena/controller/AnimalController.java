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
        
            try {
                String acao = request.getParameter("acao");
            
                if(acao == null)
                    throw new NullPointerException();
                
                GestaoAnimalService service = new GestaoAnimalService();
                
                if(acao.equals("listar")) {
                    List<Animal> animais = new ArrayList<>();
            
                    animais = service.listarAnimais();
            
                    request.setAttribute("animais", animais);
                    request.getRequestDispatcher("animais.jsp").forward(request, response);
                    
                }
                
                if(acao.equals("exibir")) {
                    String id = request.getParameter("id");
                    
                    Animal animal = service.buscarAnimal(id);
                    request.setAttribute("animal", animal);
                    request.getRequestDispatcher("exibir-animal.jsp").forward(request, response);
                }
                
                if(acao.equals("editar")) {
                    String id = request.getParameter("id");
                    
                    Animal animal = service.buscarAnimal(id);
                    request.setAttribute("animal", animal);
                    request.getRequestDispatcher("editar-animal.jsp").forward(request, response);
                }
                
                if(acao.equals("salvar_alteracoes")) {
                    String id = request.getParameter("id");
                    
                    Animal animal = service.buscarAnimal(id);
                    request.setAttribute("animal", animal);
                    request.getRequestDispatcher("exibir-animal.jsp").forward(request, response);
                }
            } catch(NullPointerException e) {
                System.out.println(e.getMessage());
            }
            
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String acao = request.getParameter("acao");
            
            if(acao == null)
                throw new NullPointerException();
                
            if(acao.equals("salvar")) {
                cadastrar(request, response);
                response.sendRedirect(request.getContextPath() + "/AnimalController?acao=listar");
            }
            
            if(acao.equals("excluir")) {
                excluir(request, response);
                response.sendRedirect(request.getContextPath() + "/AnimalController?acao=listar");
            }
            
            if(acao.equals("editar")) {
                String id = request.getParameter("id");
                editar(request, response);
                response.sendRedirect(request.getContextPath() + "/AnimalController?acao=exibir&id=" + id);
            }
        } catch(NullPointerException e) {
            System.out.println(e.getMessage());
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
    
    private void excluir(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        
        GestaoAnimalService service = new GestaoAnimalService();
        service.excluirAnimal(id);
    }
    
    /*private void exibir(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        
        GestaoAnimalService service = new GestaoAnimalService();
        service.exibirAnimal(id);
    }*/
    
    private void editar(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.valueOf(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String sexo = request.getParameter("sexo");
        String dataDeNascimento = request.getParameter("dataDeNascimento");
        Double peso = Double.valueOf(request.getParameter("peso"));
        boolean hostil = request.getParameter("hostil") != null;
            
        Animal editadoAnimal = new Animal(id, nome, sexo, dataDeNascimento, peso, hostil);
        GestaoAnimalService service = new GestaoAnimalService();
            
        service.editarAnimal(editadoAnimal);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}