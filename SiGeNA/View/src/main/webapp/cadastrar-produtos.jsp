<%@page import="sigena.model.domain.Fornecedor"%>
<%@page import="java.util.List"%>
<%@page import="sigena.model.service.GestaoFornecedorService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="sigena.model.domain.util.TipoProduto"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    pageContext.setAttribute("tiposProduto", TipoProduto.values());
    GestaoFornecedorService service = new GestaoFornecedorService();
    List<Fornecedor> fornecedores = service.listarFornecedores();
    pageContext.setAttribute("fornecedores", fornecedores);
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SiGeNA - Gestão de Produtos / Estoque</title>
        <link rel="stylesheet" href="CSS/styleprodutos.css">
        <link rel="stylesheet" href="CSS/style.css">
        <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
    </head>
    <body>
        <header class="topbar">
            <a href="HomeController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>

        <div class="container">
            <h1>Gestão de Produtos / Estoque</h1>
            <a href="ProdutoController?acao=listar" class="btn">Voltar</a>
            
            <div class="formulario">
                <h2>Adicionar Novo Item</h2>
                <form method="POST" action="ProdutoController">
                    <label for="nome">Nome do Produto:</label>
                    <input type="text" id="nome" name="nome" placeholder="Ex: Ração Premium Cães 10kg">

                    <label for="categoria">Categoria:</label>
                    <select id="categoria" name="tipoProduto">
                        <c:forEach var="tipo" items="${tiposProduto}">
                            <option value="${tipo}">${fn:replace(tipo.tipo, "_", " ")}</option>
                        </c:forEach>
                    </select>
                    
                    <label for="fornecedor">Fornecedor:</label>
                    <select id="fornecedor" name="fornecedor">
                        <c:forEach var="fornecedor" items="${fornecedores}">
                            <option value="${fornecedor.getId()}">${fornecedor.getNome()}</option>
                        </c:forEach>
                    </select>

                    <label for="quantidade">Quantidade:</label>
                    <input type="number" id="quantidade" name="quantidade" min="1" step="1" placeholder="Ex: 15">
                    
                    <div id="perecivel" style="display: block;"> 
                    <label for="lote">Data de Lote</label>
                    <input type="date" id="lote" name="lote">
                    
                    <label for="validade">Data de Validade:</label>
                    <input type="date" id="validade" name="validade">
                    </div>
                    
                    <input type="hidden" name="acao" value="salvar">
                    <button type="submit" class="btn-enviar">Salvar Item</button>
                </form>
            </div>
        </div>
    </body>
</html>

<script>
        document.getElementById("categoria").addEventListener("change", function() {
        let valor = this.value;
        
        if (valor === "PERECIVEL") {
            document.getElementById("perecivel").style.display = "block";
        } else {
            document.getElementById("perecivel").style.display = "none";
        }
    });
</script>
