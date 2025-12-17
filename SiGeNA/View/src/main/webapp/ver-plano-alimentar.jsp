<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ include file="/WEB-INF/jspf/permissoes.jspf" %>
<%
    HttpSession sessao = request.getSession(false);
    Cargo cargo = (sessao != null) ? (Cargo) sessao.getAttribute("cargoUsuario") : null;
    boolean podeGerenciar = temPermissaoGerenciamento(cargo, "planos");
    request.setAttribute("podeGerenciarPlano", podeGerenciar);
    String paginaHome = "GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "home.jsp" : "home.jsp";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SiGeNA - Plano Alimentar</title>
        <link rel="stylesheet" href="CSS/styleplanos.css">
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="<%= paginaHome %>">SiGeNA</a></div>
        </header>

        <div class="botoes-acoes">
            <a href="PlanosAlimentaresController" class="btn">Voltar</a>
            <a href="<%= paginaHome %>" class="btn">Home</a>
        </div>

        <div class="container">
            <h1>Detalhes do Plano Alimentar</h1>
            <div class="historico">
                <h2>Informações</h2>
                <p><strong>Animal:</strong> ${plano.animal.nome}</p>
                <h3>Itens do Plano</h3>
                <ul>
                    <c:forEach items="${plano.itens}" var="it">
                        <li>
                            <strong>${it.alimento}</strong> - ${it.gramatura} g, ${it.vezesPorDia}x/dia
                        </li>
                    </c:forEach>
                    <c:if test="${empty plano.itens}">
                        <li>Nenhum alimento cadastrado.</li>
                    </c:if>
                </ul>

                <c:if test="${podeGerenciarPlano}">
                    <a href="PlanosAlimentaresController?acao=editar&id=${plano.id}" class="btn">Editar</a>
                </c:if>
            </div>
        </div>
    </body>
    </html>
