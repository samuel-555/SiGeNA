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
    <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
</head>
<body>
    <header class="topbar">
            <a href="TarefaController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>

    <div class="container">
        <h1>Gestão de Espécies</h1>
        <div class="botoes-acoes">
            <% if (podeCadastrar) { %>
            <a href="cadastrar-especie.jsp" class="btn">Cadastrar Nova Espécie</a>
            <% } %>
        </div>

        <div class="historico">
            <h2>Catá­logo de Espécies</h2>

            <%
                String busca = request.getParameter("busca") != null ? request.getParameter("busca") : "";
                String predadorFiltro = request.getParameter("predador") != null ? request.getParameter("predador") : "";
                String ordem = request.getParameter("ordem") != null ? request.getParameter("ordem") : "";
            %>
            <form action="EspeciesController" method="get" class="filtro-especies">
                <input type="text" name="busca" placeholder="Buscar por nome" value="<%= busca %>">
                <select name="predador">
                    <option value="" <%= predadorFiltro.isBlank() ? "selected" : "" %>>Todas</option>
                    <option value="sim" <%= "sim".equalsIgnoreCase(predadorFiltro) ? "selected" : "" %>>Predadora</option>
                    <option value="nao" <%= "nao".equalsIgnoreCase(predadorFiltro) ? "selected" : "" %>>Nao predadora</option>
                </select>
                <select name="ordem">
                    <option value="" <%= ordem.isBlank() ? "selected" : "" %>>Ordem padrao</option>
                    <option value="nome_az" <%= "nome_az".equalsIgnoreCase(ordem) ? "selected" : "" %>>Nome A-Z</option>
                    <option value="nome_za" <%= "nome_za".equalsIgnoreCase(ordem) ? "selected" : "" %>>Nome Z-A</option>
                    <option value="mais_recente" <%= "mais_recente".equalsIgnoreCase(ordem) ? "selected" : "" %>>Mais recente</option>
                    <option value="mais_antigo" <%= "mais_antigo".equalsIgnoreCase(ordem) ? "selected" : "" %>>Mais antigo</option>
                </select>
                <button type="submit" class="btn-pequeno">Filtrar</button>
                <a href="EspeciesController" class="btn-pequeno">Limpar</a>
            </form>

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
                        <a href="EspeciesController?acao=excluir&id=<%= e.getId() %>" class="btn-pequeno excluir" onclick="return confirm('Confirmar exclusão?');">Excluir</a>
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
