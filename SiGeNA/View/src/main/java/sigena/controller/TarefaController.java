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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import sigena.model.common.exception.DatabaseException;
import sigena.model.domain.Funcionario;
import sigena.model.service.GestaoTarefaService;
import sigena.model.domain.Tarefa;
import sigena.model.service.FuncionarioService;

@WebServlet(name = "TarefaController", urlPatterns = {"/TarefaController"})
public class TarefaController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TarefaController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TarefaController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        GestaoTarefaService service = new GestaoTarefaService();
            
        String acao = request.getParameter("acao");
       
        if ("cadastrar".equals(acao)) {
            try {
                abrirFormulario(request, response);
            } catch (SQLException ex) {
                System.getLogger(TarefaController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (DatabaseException ex) {
                System.getLogger(TarefaController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            return;
        }
        if ("listar".equals(acao)) {
            listar(request, response);
        return;
    }
        
        List<Tarefa> tarefas = service.listarTarefas();
        request.setAttribute("home", tarefas);
        request.getRequestDispatcher("home-gerente.jsp").forward(request, response);
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
    
    
    public void abrirFormulario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, DatabaseException{
        FuncionarioService funcionarioService = new FuncionarioService();

        List<Funcionario> funcionarios = funcionarioService.listar();
        request.setAttribute("funcionarios", funcionarios);

        request.getRequestDispatcher("cadastrar-tarefa.jsp").forward(request, response);

    }
    
    public void cadastrar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String nome = request.getParameter("nome");
        String texto = request.getParameter("texto");
        int id_destinatario = Integer.parseInt(request.getParameter("destinatario"));
        LocalDateTime dataPConclusao = LocalDateTime.parse(request.getParameter("dataPConclusao"));
        
        GestaoTarefaService service = new GestaoTarefaService();
        
        if(!service.validarData(dataPConclusao))
            //botar erro aqui ou try ali em baixo ver oq é melhor!
        
        service.cadastrarTarefa(nome,texto,id_destinatario,dataPConclusao);
        response.sendRedirect("TarefaController");
        
    }
   
   public void editar(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String nome = request.getParameter("nome");
        String texto = request.getParameter("texto");
        int id_destinatario = Integer.parseInt(request.getParameter("destinatario"));
        LocalDateTime dataPConclusao = LocalDateTime.parse(request.getParameter("dataPConclusao"));

        long id = Integer.parseInt(request.getParameter("id"));

        GestaoTarefaService service = new GestaoTarefaService();
        service.editar(id, nome, texto, id_destinatario, dataPConclusao);

        response.sendRedirect("TarefaController");
    }

    
    public void excluir(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long id = Integer.parseInt(request.getParameter("id"));

       
        GestaoTarefaService service = new GestaoTarefaService();
        Tarefa tarefa = new Tarefa("","",0,LocalDateTime.now());
        tarefa.setId(id);
        
       
        service.excluir(tarefa);
      
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
    
    private void listar(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        GestaoTarefaService service = new GestaoTarefaService();

        List<Tarefa> tarefas = service.listarTarefas();
        request.setAttribute("home", tarefas);

        request.getRequestDispatcher("home-tarefas.jsp").forward(request, response);
    }


}
