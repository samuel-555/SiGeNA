<%@page import="java.util.List"%>
<%@page import="sigena.model.domain.PlanoAlimentar"%>
<%@page import="sigena.model.domain.Animal"%>
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
    boolean podeCadastrar = temPermissaoCadastro(cargo, "planos");
    String paginaHome = "GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "home.jsp" : "home.jsp";
    String paginaHomeComContexto = request.getContextPath() + "/" + paginaHome;
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>SiGeNA - Planos Alimentares</title>
    <link rel="stylesheet" href="CSS/styleplanos.css">
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
        <h1>Gestão de Planos Alimentares</h1>
        <div class="botoes-acoes">
            <% if (podeCadastrar) { %>
            <a href="PlanosAlimentaresController?acao=cadastrar" class="btn">Cadastrar Novo Plano</a>
            <% } %>
            <a href="<%= paginaHomeComContexto %>" class="btn">Voltar para Home</a>
        </div>
        <div class="filtros">
            <form action="PlanosAlimentaresController" method="get">
                <label for="animalId">Animal:</label>
                <select name="animalId" id="animalId">
                    <option value="">Todos</option>
                    <% List<Animal> animaisFiltro = (List<Animal>) request.getAttribute("animais");
                       Long animalSelecionado = (Long) request.getAttribute("animalSelecionado");
                       if (animaisFiltro != null) {
                           for (Animal a : animaisFiltro) { %>
                        <option value="<%= a.getId() %>" <%= (animalSelecionado != null && animalSelecionado.equals(a.getId())) ? "selected" : "" %>><%= a.getNome() %></option>
                    <%     }
                       } %>
                </select>

                <label for="ingrediente">Ingrediente:</label>
                <input type="text" name="ingrediente" id="ingrediente" placeholder="Ex: carne" value="<%= request.getAttribute("ingredienteFiltro") != null ? request.getAttribute("ingredienteFiltro") : "" %>">

                <button type="submit" class="btn">Filtrar</button>
                <a href="PlanosAlimentaresController" class="btn">Limpar</a>
            </form>
        </div>
        <div class="historico">
            <h2>Lista de Planos Alimentares</h2>

            <% String erro = (String) request.getAttribute("erro");
               if (erro != null) { %>
                <p style="color:red;"><%= erro %></p>
            <% } %>

            <table border="1" width="100%">
                <tr>
                    <th>Animal</th>
                    <th>Itens</th>
                    <th>Ações</th>
                </tr>

                <%
                    List<PlanoAlimentar> lista = (List<PlanoAlimentar>) request.getAttribute("lista");
                    if (lista != null && !lista.isEmpty()) {
                        for (PlanoAlimentar p : lista) {
                %>
                <tr>
                    <td><%= (p.getAnimal() != null ? p.getAnimal().getNome() : "-") %></td>
                    <td>
                        <ul>
                            <%
                                if (p.getItens() != null && !p.getItens().isEmpty()) {
                                    for (sigena.model.domain.ItemPlanoAlimentar it : p.getItens()) {
                            %>
                                <li><%= it.getAlimento() %> - <%= it.getGramatura() %> g, <%= it.getVezesPorDia() %>x/dia</li>
                            <%
                                    }
                                } else {
                            %>
                                <li>Nenhum alimento</li>
                            <%
                                }
                            %>
                        </ul>
                    </td>
                    <td>
                        <a href="PlanosAlimentaresController?acao=ver&id=<%= p.getId() %>" class="btn-pequeno ver">Ver</a>
                        <% if (podeCadastrar) { %>
                        <a href="PlanosAlimentaresController?acao=excluir&id=<%= p.getId() %>" class="btn-pequeno excluir" onclick="return confirm('Confirmar cancelamento?');">Cancelar</a>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr><td colspan="5">Nenhum plano alimentar cadastrado.</td></tr>
                <%
                    }
                %>
            </table>
        </div>
    </div>
</body>
</html>
