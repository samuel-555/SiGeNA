package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Function;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Animal;
import sigena.model.domain.Especie;
import sigena.model.domain.Habitat;
import sigena.model.service.GestaoAnimalService;
import sigena.model.service.GestaoEspeciesService;
import sigena.model.service.GestaoHabitatService;
import sigena.model.common.util.StringUtils;
import sigena.controller.util.ListOrdener;
import sigena.model.service.GestaoNotificacaoService;

@WebServlet(name = "AnimalController", urlPatterns = {"/AnimalController"})
public class AnimalController extends HttpServlet {
    private final GestaoAnimalService service = new GestaoAnimalService();
    private final GestaoHabitatService consultaHabitat = new GestaoHabitatService();
    private final GestaoEspeciesService consultaEspecie = new GestaoEspeciesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            try {
                String acao = request.getParameter("acao");
                if(acao == null)
                    throw new NullPointerException();
                
                if("listar".equals(acao)) {
                    List<Animal> animais = null;
                    String busca = StringUtils.conferNull(request.getParameter("busca"));
                    String filtro = StringUtils.conferNull(request.getParameter("filtro"));
                    String sequencia = StringUtils.conferNull(request.getParameter("sequencia"));
                    String ordem = StringUtils.conferNull(request.getParameter("ordem"));
                    
                    animais = service.listarAnimais(busca, filtro);
                    
                    ListOrdener.ordenarBusca(animais, sequencia, ordem, Animal::getNome);
                    List<Especie> especies = null;
                    List<Habitat> habitats = null;
                    
                    try {
                        especies = consultaEspecie.listar();
                        habitats = consultaHabitat.listarHabitats();
                    } catch(PersistenciaException e) {
                        request.setAttribute("erro", e.getMessage());
                    }
                    
                    request.setAttribute("especies", especies);
                    request.setAttribute("habitats", habitats);
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
                    List<Habitat> habitats = null;
                    
                    try {
                        especies = consultaEspecie.listar();
                        habitats = consultaHabitat.listarHabitats();
                    } catch(PersistenciaException e) {
                        request.setAttribute("erro", e.getMessage());
                    }
                    request.setAttribute("especies", especies);
                    request.setAttribute("habitats", habitats);
                    Animal animal = service.buscarAnimal(id);
                    request.setAttribute("animal", animal);
                    request.setAttribute("habitats", habitats);
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
                    List<Habitat> habitats = null;
                    
                    try {
                        especies = consultaEspecie.listar();
                        habitats = consultaHabitat.listarHabitats();
                    } catch(PersistenciaException e) {
                        request.setAttribute("erro", e.getMessage());
                    }
                    
                    request.setAttribute("especies", especies);
                    request.setAttribute("habitats", habitats);
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
                boolean success = cadastrar(request, response);
                HttpSession sessao = request.getSession(false);
                if(!success) {
                    sessao.setAttribute("campoInvalidoErro", "Campo(s) inválido(s) preenchido(s)!");
                    response.sendRedirect(request.getContextPath() + "/AnimalController?acao=cadastrar");
                    return;
                }
                GestaoNotificacaoService not = new GestaoNotificacaoService();
                not.criarParaTodos("Novo animal cadastrado");
                sessao.setAttribute("acaoBemSucedida", "Animal cadastrado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/AnimalController?acao=listar");
                return;
            }
            
            if("excluir".equals(acao)) {
                excluir(request, response);
                response.sendRedirect(request.getContextPath() + "/AnimalController?acao=listar");
            }
            
            if("editar".equals(acao)) {
                boolean success = editar(request, response);
                String id = request.getParameter("id");
                HttpSession sessao = request.getSession(false);
                if(!success) {
                    sessao.setAttribute("campoInvalidoErro", "Campo(s) inválido(s) preenchido(s)!");
                    response.sendRedirect(request.getContextPath() + "/AnimalController?acao=editar&id=" + id);
                    return;
                }
                sessao.setAttribute("acaoBemSucedida", "Animal editado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/AnimalController?acao=exibir&id=" + id);
            }
        } catch(PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private boolean cadastrar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException, IOException, ServletException{
        String nome = request.getParameter("nome");
        int especieId = Integer.parseInt(request.getParameter("especie"));
        Especie especie = consultaEspecie.buscarPorId(especieId);
        String sexo = request.getParameter("sexo");
        String dataDeNascimento = request.getParameter("dataDeNascimento");
        Double peso = Double.valueOf(request.getParameter("peso"));
        boolean hostil = request.getParameter("hostil") != null;
        Habitat habitat = consultaHabitat.buscar(request.getParameter("habitat"));

        Animal novoAnimal = new Animal(nome, especie, sexo, dataDeNascimento, peso, hostil, habitat);
        
        return service.cadastrarAnimal(novoAnimal);
    }
    
    private void excluir(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException{
        Long id = Long.valueOf(request.getParameter("id"));
        service.excluirAnimal(id);
    }
    
    private boolean editar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException{
        Long id = Long.valueOf(request.getParameter("id"));
        String nome = request.getParameter("nome");
        int especieId = Integer.parseInt(request.getParameter("especie"));
        Especie especie = consultaEspecie.buscarPorId(especieId);
        String sexo = request.getParameter("sexo");
        String dataDeNascimento = request.getParameter("dataDeNascimento");
        Double peso = Double.valueOf(request.getParameter("peso"));
        boolean hostil = request.getParameter("hostil") != null;
        Habitat habitat = consultaHabitat.buscar(request.getParameter("habitat"));
        
        Animal editadoAnimal = new Animal(id, nome, especie, sexo, dataDeNascimento, peso, hostil, habitat);
            
        return service.editarAnimal(editadoAnimal);
    }
}