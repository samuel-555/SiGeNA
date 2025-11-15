<%-- 
    Document   : tratamentos
    Created on : 3 de nov. de 2025, 08:17:27
    Author     : aluno
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tratamentos</title>
        <link rel="stylesheet" href="CSS/styletratamentos.css">
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
  <header>
    <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Gestão de Tratamentos Médicos</h1>

    <div class="botoes-acoes">
        <button class="btn"><a href="novoTratamento.jsp">Registrar Novo Tratamento</a></button>
    </div>

     <c:if test="${not empty mensagemSucesso}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${mensagemSucesso}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty mensagemErro}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${mensagemErro}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    </div>
  </div>
</body>
</html>
