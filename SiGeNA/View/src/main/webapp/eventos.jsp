<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@taglib uri="jakarta.tags.xml" prefix="x" %>
<%@taglib uri="jakarta.tags.sql" prefix="sql"%>

<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Gestão de Eventos</title>
  <link rel="stylesheet" href="CSS\style.css">
  <link rel="stylesheet" href="CSS\styleanimais.css">
</head>
<body>
  <header>
    <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Gestão de Eventos</h1>
    <div class="botoes-acoes">
        <a href="EventoController?acao=cadastrar" class="btn">Cadastrar Novo Animal</a>
    </div>

    <c:if test="${empty eventos}">
        <p>Nenhum evento encontrado.</p>
    </c:if>
    <c:if test="${not empty sessionScope.acaoBemSucedida}">
        <p class="sucesso"><c:out value="${sessionScope.acaoBemSucedida}"/></p>
        <c:remove var="acaoBemSucedida" scope="session"/>
    </c:if>
    <c:if test="${not empty eventos}">
        <div class="historico">
        <h2>Lista de Eventos</h2>

            <c:forEach var="evento" items="${eventos}">
                <div class="evento">
                  <h2><c:out value="${evento.dataProgramadaFormat}"/> - <c:out value="${evento.horaProgramadaFormat}"/></h2>
                  <h3><c:out value="${evento.titulo}"/></h3>
                  <p><c:out value="${evento.descricao}"/></p>
                </div>
            </c:forEach>
        
        </div>     
    </c:if>
    
  </div>
</body>
</html>

