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
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Animal;
import sigena.model.domain.Especie;
import sigena.model.service.GestaoAnimalService;
import sigena.model.dao.EspecieDAO;

@WebServlet(name = "AnimalController", urlPatterns = {"/AnimalController"})
public class AnimalController extends HttpServlet {
    GestaoAnimalService service = new GestaoAnimalService();
    EspecieDAO consultaEspecie = new EspecieDAO();
    
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
                
                if("listar".equals(acao)) {
                    List<Animal> animais = new ArrayList<>();
                    animais = service.listarAnimais();
                    request.setAttribute("animais", animais);
                    request.getRequestDispatcher("animais.jsp").forward(request, response);
                }
                
                if("exibir".equals(acao)) {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Animal animal = service.buscarAnimal(id);
                    request.setAttribute("animal", animal);
                    request.getRequestDispatcher("exibir-animal.jsp").forward(request, response);
                }
                
                if("editar".equals(acao)) {
                    Long id = Long.valueOf(request.getParameter("id"));
                    List<Especie> especies = null;
                    try {
                        especies = consultaEspecie.listar();
                    } catch(PersistenciaException e) {
                        request.setAttribute("erro", e.getMessage());
                    }
                    request.setAttribute("especies", especies);
                    Animal animal = service.buscarAnimal(id);
                    request.setAttribute("animal", animal);
                    request.getRequestDispatcher("editar-animal.jsp").forward(request, response);
                }
                
                if("salvar_alteracoes".equals(acao)) {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Animal animal = service.buscarAnimal(id);
                    request.setAttribute("animal", animal);
                    request.getRequestDispatcher("exibir-animal.jsp").forward(request, response);
                }
                
                if("cadastrar".equals(acao)) {
                    List<Especie> especies = null;
                    try {
                        especies = consultaEspecie.listar();
                    } catch(PersistenciaException e) {
                        System.out.println(e.getMessage());
                    }
                    
                    request.setAttribute("especies", especies);
                    request.getRequestDispatcher("cadastrar-animal.jsp").forward(request, response);
                }
                
            } catch(PersistenciaException e) {
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
                
            if("salvar".equals(acao)) {
                cadastrar(request, response);
                response.sendRedirect(request.getContextPath() + "/AnimalController?acao=listar");
            }
            
            if("excluir".equals(acao)) {
                excluir(request, response);
                response.sendRedirect(request.getContextPath() + "/AnimalController?acao=listar");
            }
            
            if("editar".equals(acao)) {
                String id = request.getParameter("id");
                editar(request, response);
                response.sendRedirect(request.getContextPath() + "/AnimalController?acao=exibir&id=" + id);
            }
        } catch(PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void cadastrar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException, IOException, ServletException{
        String nome = request.getParameter("nome");
        int especieId = Integer.parseInt(request.getParameter("especie"));
        
        Especie especie = consultaEspecie.buscarPorId(especieId);
        
        String sexo = request.getParameter("sexo");
        String dataDeNascimento = request.getParameter("dataDeNascimento");
        Double peso = Double.valueOf(request.getParameter("peso"));
        boolean hostil = request.getParameter("hostil") != null;
        Animal novoAnimal = new Animal(nome, especie, sexo, dataDeNascimento, peso, hostil);
        GestaoAnimalService service = new GestaoAnimalService();
            
        boolean result = service.cadastrarAnimal(novoAnimal);
        
        if(!result) {
            List<Especie> especies = null;
            try {
                especies = consultaEspecie.listar();
            } catch(PersistenciaException e) {
                System.out.println(e.getMessage());
            }
                    
            request.setAttribute("especies", especies);
            request.setAttribute("erro", "Não foi possível cadastrar o animal pois ele não foi associado a uma espécie.");
            request.getRequestDispatcher("cadastrar-animal.jsp").forward(request, response);
        }
    }
    
    private void excluir(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException{
        Long id = Long.valueOf(request.getParameter("id"));
        GestaoAnimalService service = new GestaoAnimalService();
        service.excluirAnimal(id);
    }
    
    private void editar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException{
        Long id = Long.valueOf(request.getParameter("id"));
        String nome = request.getParameter("nome");
        int especieId = Integer.parseInt(request.getParameter("especie"));
        Especie especie = null;
        especie = consultaEspecie.buscarPorId(especieId);
        String sexo = request.getParameter("sexo");
        String dataDeNascimento = request.getParameter("dataDeNascimento");
        Double peso = Double.valueOf(request.getParameter("peso"));
        boolean hostil = request.getParameter("hostil") != null;
            
        Animal editadoAnimal = new Animal(id, nome, especie, sexo, dataDeNascimento, peso, hostil);
        GestaoAnimalService service = new GestaoAnimalService();
            
        service.editarAnimal(editadoAnimal);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}