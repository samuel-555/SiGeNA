<%-- 
    Document   : historico
    Created on : 8 de dez. de 2025, 10:54:38
    Author     : USUARIO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

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
  <title>SiGeNA - Gestão de Habitat</title>
  <link rel="stylesheet" href="CSS\style.css">
  <link rel="stylesheet" href="CSS\stylehabitat.css">
</head>

<body>
  <header>
    <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Gestão de Histórico</h1>
    
    <div class="lista-de-habitats">
      <h2>Histórico</h2>
      
    <form id="formBusca" action="HistoricoController" method="get">
        
        <input type="hidden" name="acao" value="buscar">
        <input type="text"name="q"placeholder="Buscar por funcionario, cargo ou tipo"onkeyup="buscar()">
        
    </form>

    <script>
        let timer;
        function buscar() {
            clearTimeout(timer);
            timer = setTimeout(() => {
                document.getElementById("formBusca").submit();
            }, 500);
        }
    </script>

    <c:if test="${not empty historico}">
  <table>
    <thead>
      <tr>
        <th>Tipo</th>
        <th>Descrição</th>
        <th>Data</th>
      </tr>
    </thead>
    
    <tbody>
        <c:forEach var="hist" items="${historico}"> <!-- separar na coisa coisa coisa coisa coisa funcionarios somehow -->
     
          <tr>
            <td>${hist.tipo}</td>
            <td>${hist.descricao}</td>
            <fmt:formatDate value="${hist.data}" pattern="dd/MM/yyyy HH:mm"/>
          </tr>
        
        </c:forEach>
    </tbody>
    </table>
    </c:if>

    </div>
  </div>
</body>
</html>
