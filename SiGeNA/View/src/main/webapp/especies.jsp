<%@page import="java.util.List"%>
<%@page import="sigena.model.domain.Especie"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ include file="/WEB-INF/jspf/permissoes.jspf" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    boolean podeCadastrar = temPermissaoCadastro(cargo, "especies");
    boolean podeGerenciar = temPermissaoGerenciamento(cargo, "especies");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>SiGeNA - Gestão de Espécies</title>
    <link rel="stylesheet" href="CSS/styleespecies.css">
    <link rel="stylesheet" href="CSS\style.css">
</head>
<body>
    <header><div class="titulo"><a href="<%= request.getContextPath() + "/home.jsp" %>">SiGeNA</a></div></header>

    <div class="container">
        <h1>Gestão de Espécies</h1>
        <div class="botoes-acoes">
            <% if (podeCadastrar) { %>
            <a href="cadastrar-especie.jsp" class="btn">Cadastrar Nova Espécie</a>
            <% } %>
        </div>

        <div class="historico">
            <h2>Catá­logo de Espécies</h2>

            <% String erro = (String) request.getAttribute("erro");
               if (erro != null) { %>
                <p style="color:red;"><%= erro %></p>
            <% } %>

            <table border="1" width="100%">
                <tr>
                    <th>Nome</th>
                    <th>Classe</th>
                    <th>Habitat</th>
                    <th>Alimentação</th>
                    <th>Predador</th>
                    <th>Ações</th>
                </tr>

                <%
                    List<Especie> lista = (List<Especie>) request.getAttribute("lista");
                    if (lista != null && !lista.isEmpty()) {
                        for (Especie e : lista) {
                %>
                <tr>
                    <td><%= e.getNome() %></td>
                    <td><%= e.getClasse() %></td>
                    <td><%= e.getHabitat() %></td>
                    <td><%= e.getAlimentacao() %></td>
                    <td><%= e.isPredador() ? "Sim" : "Não" %></td>
                    <td>
                        <a href="EspeciesController?acao=ver&id=<%= e.getId() %>" class="btn-pequeno ver">Ver</a>
                        <% if (podeGerenciar) { %>
                        <a href="EspeciesController?acao=excluir&id=<%= e.getId() %>" class="btn-pequeno excluir">Excluir</a>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr><td colspan="6">Nenhuma espécie cadastrada.</td></tr>
                <%
                    }
                %>
            </table>
        </div>
    </div>
</body>
</html>
