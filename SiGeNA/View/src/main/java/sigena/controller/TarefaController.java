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
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sigena.model.common.exception.DataInvalidaException;
import sigena.model.common.exception.DatabaseException;
import sigena.model.domain.Cargo;
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
        HttpSession sessao = request.getSession(false);
        
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
        Integer idUsuario = (Integer) sessao.getAttribute("idUsuario");
        Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
        
        try {
            List<Tarefa> tarefas;

            if (cargo != null && cargo.getDescricao().equalsIgnoreCase("GERENTE"))
                tarefas = service.listarTarefas();
            else 
                tarefas = service.listarPorUsuario(idUsuario);
        request.setAttribute("tarefas", tarefas);
        request.getRequestDispatcher("home.jsp").forward(request, response);

        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        switch(acao){
            case "inserir":
            {
                try {
                    cadastrar(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(TarefaController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DatabaseException ex) {
                    Logger.getLogger(TarefaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                break;

            case "editar":
            {
                try {
                    editar(request, response);
                } catch (DataInvalidaException ex) {
                    System.getLogger(TarefaController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                } catch (SQLException ex) {
                System.getLogger(TarefaController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (DatabaseException ex) {
                System.getLogger(TarefaController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            }
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
    
    public void cadastrar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException, DatabaseException{
        String nome = request.getParameter("nome");
        String texto = request.getParameter("texto");
        int id_destinatario = Integer.parseInt(request.getParameter("destinatario"));
        String dataStr = request.getParameter("data-conclusao");
        DateTimeFormatter dataForm = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dataPConclusao = LocalDateTime.parse(dataStr, dataForm);

        
        GestaoTarefaService service = new GestaoTarefaService();
      
        try {
            service.cadastrarTarefa(nome,texto,id_destinatario,dataPConclusao);
        } 
        catch (DataInvalidaException ex) {    
            request.setAttribute("msgErro",ex.getMessage());
            abrirFormulario(request, response);
            return;
        }
        response.sendRedirect("TarefaController");
        
    }
   
   public void editar(HttpServletRequest request, HttpServletResponse response) throws IOException, DataInvalidaException, ServletException, SQLException, DatabaseException{

        String nome = request.getParameter("nome");
        String texto = request.getParameter("texto");
        int id_destinatario = Integer.parseInt(request.getParameter("destinatario"));
        String dataStr = request.getParameter("data-conclusao");
        DateTimeFormatter dataForm = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dataPConclusao = LocalDateTime.parse(dataStr, dataForm);

        FuncionarioService funcionarioService = new FuncionarioService();
        Funcionario funcionario = funcionarioService.buscarPorId(id_destinatario);
        String nomeFuncionario = funcionario.getNome();
        
        long id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("destinatario",nomeFuncionario);

        GestaoTarefaService service = new GestaoTarefaService();
        try{
            service.editar(id, nome, texto, id_destinatario, dataPConclusao);
        }
        catch (DataInvalidaException ex) {    
            request.setAttribute("msgErro",ex.getMessage());
            request.getRequestDispatcher("cadastrar-tarefa.jsp").forward(request, response);
            return;
        }
        

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
        request.setAttribute("tarefas", tarefas);

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }


}
