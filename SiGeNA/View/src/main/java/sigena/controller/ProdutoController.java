package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Produto;
import sigena.model.domain.util.TipoProduto;
import sigena.model.service.GestaoProdutoService;

@WebServlet(name = "ProdutoController", urlPatterns = {"/ProdutoController"})
public class ProdutoController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProdutoController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProdutoController at " + request.getContextPath() + "</h1>");
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
        try {
            String acao = request.getParameter("acao");
            if(acao == null){
                throw new NullPointerException();
            }
            if ("salvar".equals(acao)) {
                cadastrar(request);
                
            }
        }catch(PersistenciaException e) {
            System.out.println(e.getMessage());
        }

    }

    public void cadastrar(HttpServletRequest request) throws PersistenciaException{
        /*GestaoFornecedorService serviceF = new GestaoFornecedorService();
        Long FornecedorId = Long.valueOf(request.getParameter("fornecedor"));
        Fornecedor fornecedor = serviceF.buscarFornecedor(FornecedorId);*/
        String nome = request.getParameter("nome");
        
        int quantidade = 0;
        try {
            quantidade = Integer.parseInt(request.getParameter("quantidade"));
        } catch (NumberFormatException e) {
            quantidade = 0;
        }
        LocalDate validade = LocalDate.parse(request.getParameter("validade"));
        LocalDate lote = LocalDate.parse(request.getParameter("lote"));
        TipoProduto tipo = TipoProduto.valueOf(request.getParameter("tipoProduto"));
        
        
        Produto produto = new Produto(nome, /*fornecedor,*/ quantidade, validade, lote, tipo);
        GestaoProdutoService service = new GestaoProdutoService();
        service.cadastrar(produto);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
