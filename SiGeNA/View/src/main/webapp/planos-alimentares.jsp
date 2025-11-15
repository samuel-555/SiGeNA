<%@page import="java.util.List"%>
<%@page import="sigena.model.domain.PlanoAlimentar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String paginaHome = "GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "home-gerente.jsp" : "home.jsp";
    String paginaHomeComContexto = request.getContextPath() + "/" + paginaHome;
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>SiGeNA - Planos Alimentares</title>
    <link rel="stylesheet" href="CSS/styleplanos.css">
    <link rel="stylesheet" href="CSS/style.css">
    </head>
<body>
    <header><div class="titulo"><a href="<%= paginaHome %>">SiGeNA</a></div></header>

    <div class="container">
        <h1>Gestão de Planos Alimentares</h1>
        <div class="botoes-acoes">
            <a href="PlanosAlimentaresController?acao=cadastrar" class="btn">Cadastrar Novo Plano</a>
            <a href="<%= paginaHomeComContexto %>" class="btn">Voltar à Home</a>
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
                        <a href="PlanosAlimentaresController?acao=excluir&id=<%= p.getId() %>" class="btn-pequeno excluir">Excluir</a>
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
