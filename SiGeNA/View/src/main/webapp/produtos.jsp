<%@page import="java.util.List"%>
<%@page import="sigena.model.domain.Produto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String paginaHome = "GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) 
        ? "home-gerente.jsp" 
        : "home.jsp";

    String paginaHomeComContexto = request.getContextPath() + "/" + paginaHome;
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>SiGeNA - Produtos / Estoque</title>
    <link rel="stylesheet" href="CSS/styleprodutos.css">
    <link rel="stylesheet" href="CSS/style.css">
</head>

<body>
<header>
    <div class="titulo"><a href="<%= paginaHome %>">SiGeNA</a></div>
</header>

<div class="container">
    <h1>Gestão de Produtos e Estoque</h1>

    <div class="botoes-acoes">
        <a href="cadastrar-produtos.jsp" class="btn">Adicionar Novo Produto</a>
        <a href="<%= paginaHomeComContexto %>" class="btn">Voltar à Home</a>
    </div>

    <div class="historico">
        <h2>Lista de Produtos</h2>

        <% 
            String erro = (String) request.getAttribute("erro");
            if (erro != null) { 
        %>
            <p style="color:red;"><%= erro %></p>
        <% } %>

        <table border="1" width="100%">
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Tipo</th>
                <th>Quantidade</th>
                <th>Lote</th>
                <th>Validade</th>
                <th>Disponível?</th>
                <th>Ações</th>
            </tr>

            <%
                List<Produto> lista = (List<Produto>) request.getAttribute("lista");

                if (lista != null && !lista.isEmpty()) {
                    for (Produto p : lista) {
            %>

            <tr>
                <td><%= p.getId() %></td>
                <td><%= p.getNome() %></td>
                <td><%= p.getTipo().getTipo() %></td>
                <td><%= p.getQuantidade() %></td>
                <td><%= p.getLote() != null ? p.getLote() : "-" %></td>
                <td><%= p.getValidade() != null ? p.getValidade() : "-" %></td>
                <td><%= p.getDisponivel() ? "Sim" : "Não" %></td>

                <td>
                    <a href="ProdutoController?acao=ver&id=<%= p.getId() %>" class="btn-pequeno ver">Ver</a>
                    <a href="ProdutoController?acao=excluir&id=<%= p.getId() %>" 
                       onclick="return confirm('Excluir este produto?');" 
                       class="btn-pequeno excluir">Excluir</a>
                </td>
            </tr>

            <%
                    }
                } else {
            %>

            <tr><td colspan="8">Nenhum produto cadastrado.</td></tr>

            <% } %>
        </table>
    </div>
</div>

</body>
</html>
