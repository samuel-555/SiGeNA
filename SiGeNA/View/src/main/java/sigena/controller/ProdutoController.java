package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Fornecedor;
import sigena.model.domain.Notificacao;
import sigena.model.domain.Produto;
import sigena.model.domain.Usuario;
import sigena.model.domain.util.TipoProduto;
import sigena.model.service.GestaoFornecedorService;
import sigena.model.service.GestaoNotificacaoService;
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
        String acao = request.getParameter("acao");

        try {
            if (acao == null || acao.equals("listar")) {
                listar(request, response);
            } else if (acao.equals("excluir")) {
                excluir(request, response);
            } else if (acao.equals("ver")) {
                ver(request, response);
            }
        } catch (PersistenciaException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("produtos.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        try {
            if (acao == null) {
                throw new NullPointerException();
            }
            if ("salvar".equals(acao)) {
                cadastrar(request, response);
                GestaoNotificacaoService not = new GestaoNotificacaoService();
                not.criarParaTodos("Novo produto cadastrado");
            } else if ("salvarEdicao".equals(acao)) {
                editar(request, response);
            }
            response.sendRedirect("ProdutoController?acao=listar");
        } catch (PersistenciaException e) {
            response.sendRedirect("ProdutoController?acao=listar&erro="
                    + URLEncoder.encode(e.getMessage(), "UTF-8")
            );
        }

    }

    public void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {
        GestaoFornecedorService serviceF = new GestaoFornecedorService();
        Long FornecedorId = Long.valueOf(request.getParameter("fornecedor"));
        Fornecedor fornecedor = serviceF.buscarFornecedor(FornecedorId);
        String nome = request.getParameter("nome");

        int quantidade;
        try {
            quantidade = Integer.parseInt(request.getParameter("quantidade"));
        } catch (NumberFormatException e) {
            quantidade = 0;
        }

        TipoProduto tipo = TipoProduto.valueOf(request.getParameter("tipoProduto"));

        String validadeStr = request.getParameter("validade");
        String loteStr = request.getParameter("lote");

        LocalDate validade = null;
        LocalDate lote = null;

        if (tipo.name().equalsIgnoreCase("Perecivel")) {

            if (validadeStr != null && !validadeStr.isBlank()) {
                validade = LocalDate.parse(validadeStr);
            }

            if (loteStr != null && !loteStr.isBlank()) {
                lote = LocalDate.parse(loteStr);
            }
        } else {
            validade = LocalDate.of(9999, 12, 31);
            lote = LocalDate.of(9999, 12, 31);
        }

        Produto produto = new Produto(nome, fornecedor, quantidade, validade, lote, tipo);
        GestaoProdutoService service = new GestaoProdutoService();
        service.cadastrar(produto, fornecedor);
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {

        GestaoProdutoService service = new GestaoProdutoService();

        List<Produto> lista = service.listar();
        LocalDate hoje = LocalDate.now();

        for (Produto p : lista) {
            LocalDate validade = p.getValidade();
            if (validade.isEqual(hoje) || validade.isBefore(hoje)) {
                if (p.getDisponivel()) {
                    p.setDisponivel(false);
                    service.alterar(p);
                }
            }
        }

        lista = service.listar();

        request.setAttribute("lista", lista);
        request.getRequestDispatcher("produtos.jsp").forward(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {

        Long id = Long.parseLong(request.getParameter("id"));

        GestaoProdutoService service = new GestaoProdutoService();
        service.excluir(id);

        response.sendRedirect("ProdutoController?acao=listar");
    }

    private void ver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {

        Long id = Long.parseLong(request.getParameter("id"));

        GestaoProdutoService service = new GestaoProdutoService();
        Produto p = service.buscar(id);

        request.setAttribute("produto", p);
        request.getRequestDispatcher("editar-produto.jsp").forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, PersistenciaException {

        Long id = Long.valueOf(request.getParameter("id"));
        String nome = request.getParameter("nome");

        int quantidade;
        try {
            quantidade = Integer.parseInt(request.getParameter("quantidade"));
        } catch (Exception e) {
            quantidade = 0;
        }

        TipoProduto tipo = TipoProduto.valueOf(request.getParameter("tipoProduto"));
        String validadeStr = request.getParameter("validade");
        String loteStr = request.getParameter("lote");

        LocalDate validade = null;
        LocalDate lote = null;

        if (tipo.name().equalsIgnoreCase("Perecivel")) {
            if (validadeStr != null && !validadeStr.isBlank()) {
                validade = LocalDate.parse(validadeStr);
            }
            if (loteStr != null && !loteStr.isBlank()) {
                lote = LocalDate.parse(loteStr);
            }
        } else {
            validade = LocalDate.of(9999, 12, 31);
            lote = LocalDate.of(9999, 12, 31);
        }

        boolean disponivel = request.getParameter("disponivel") != null;

        GestaoProdutoService service = new GestaoProdutoService();
        Produto produto = service.buscar(id);

        produto.setNome(nome);
        produto.setQuantidade(quantidade);
        produto.setTipo(tipo);
        produto.setValidade(validade);
        produto.setLote(lote);
        produto.setDisponivel(disponivel);

        service.alterar(produto);

        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
