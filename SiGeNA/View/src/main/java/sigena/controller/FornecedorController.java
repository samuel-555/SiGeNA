package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Fornecedor;
import sigena.model.service.GestaoFornecedorService;

@WebServlet(name = "FornecedorController", urlPatterns = {"/FornecedorController"})
public class FornecedorController extends HttpServlet {
    private final GestaoFornecedorService service = new GestaoFornecedorService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
                String acao = request.getParameter("acao");
                
                if("listar".equals(acao)) {
                    List<Fornecedor> fornecedores = null;
                    fornecedores = service.listarFornecedores();
                    request.setAttribute("fornecedores", fornecedores);
                    request.getRequestDispatcher("fornecedores.jsp").forward(request, response);
                }
                
                if("exibir".equals(acao)) {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Fornecedor fornecedor = service.buscarFornecedor(id);
                    request.setAttribute("fornecedor", fornecedor);
                    request.getRequestDispatcher("exibir-fornecedor.jsp").forward(request, response);
                }
                
                if("editar".equals(acao)) {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Fornecedor fornecedor = service.buscarFornecedor(id);
                    request.setAttribute("fornecedor", fornecedor);
                    request.getRequestDispatcher("editar-fornecedor.jsp").forward(request, response);
                }
                
                if("salvar_alteracoes".equals(acao)) {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Fornecedor fornecedor = service.buscarFornecedor(id);
                    request.setAttribute("fornecedor", fornecedor);
                    request.getRequestDispatcher("exibir-fornecedor.jsp").forward(request, response);
                }
                
                if("cadastrar".equals(acao)) {
                    request.getRequestDispatcher("cadastrar-fornecedor.jsp").forward(request, response);
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
                    response.sendRedirect(request.getContextPath() + "/FornecedorController?acao=cadastrar");
                    return;
                }
                
                sessao.setAttribute("acaoBemSucedida", "Fornecedor cadastrado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/FornecedorController?acao=listar");
                return;
            }
            
            if("excluir".equals(acao)) {
                excluir(request, response);
                response.sendRedirect(request.getContextPath() + "/FornecedorController?acao=listar");
            }
            
            if("editar".equals(acao)) {
                boolean success = editar(request, response);
                String id = request.getParameter("id");
                HttpSession sessao = request.getSession(false);
                if(!success) {
                    sessao.setAttribute("campoInvalidoErro", "Campo(s) inválido(s) preenchido(s)!");
                    response.sendRedirect(request.getContextPath() + "/FornecedorController?acao=editar&id=" + id);
                    return;
                }
                sessao.setAttribute("acaoBemSucedida", "Fornecedor editado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/FornecedorController?acao=exibir&id=" + id);
            }
        } catch(PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private boolean cadastrar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException, IOException, ServletException {
        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String email = request.getParameter("email");
        String endereco = request.getParameter("endereco");
        String tipo = request.getParameter("tipo");
        String descricao = request.getParameter("descricao");
        
        Fornecedor fornecedor = new Fornecedor(nome, telefone, email, endereco, tipo, descricao);
        
        return service.cadastrarFornecedor(fornecedor);
    }
    
    private void excluir(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException{ 
        Long id = Long.valueOf(request.getParameter("id"));
        service.excluirFornecedor(id);
    }
    
    private boolean editar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException, IOException, ServletException {
        Long id = Long.valueOf(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String email = request.getParameter("email");
        String endereco = request.getParameter("endereco");
        String tipo = request.getParameter("tipo");
        String descricao = request.getParameter("descricao");
        
        Fornecedor fornecedor = new Fornecedor(id, nome, telefone, email, endereco, tipo, descricao);
        
        return service.editarFornecedor(fornecedor);
    }
}
