<%-- 
    Document   : habitats
    Created on : 3 de nov. de 2025, 09:26:00
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
    <div class="titulo"><a href="home.jsp">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Gestão de Habitat</h1>

    <div class="botoes-acoes">
      <a href="cadastrar-habitat.jsp" class="btn">Cadastrar Novo Habitat</a>
    </div>

    
    <div class="lista-de-habitats">
      <h2>Lista de Habitats</h2>
      
    <c:if test="${empty habitats}">
        <p>Nenhum habitat cadastrado.</p>
    </c:if>
    <c:if test="${not empty habitats}">
  <table>
    <thead>
      <tr>
        <th>Nome</th>
        <th>Tipo</th>
        <th>Tamanho</th>
        <th>Precisa de Manutenção</th>
        <th>Disponibilidade</th>
        <th>Ações</th>
      </tr>
    </thead>

    <tbody>
        <c:forEach var="habitat" items="${habitats}">

          <tr>
            <td>${habitat.nome}</td>
            <td>${habitat.tipo}</td>
            <td>${habitat.tamanho}</td>
            <td>${habitat.manutencao ? "Sim" : "Não"}</td>
            <td>${habitat.disponivel ? "Disponível":"Indisponível"}</td>
            <td>
                
              <a href="HabitatController?acao=editar&nome=${habitat.nome}" class="btn-pequeno editar">Editar</a>


              <form action="${pageContext.request.contextPath}/HabitatController" method="POST" style="display:inline-block;">
                  <input type="hidden" name="acao" value="excluir">
                  <input type="hidden" name="nome" value="${habitat.nome}">
                  <button class="btn-pequeno excluir">Excluir</button>
             </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>

    </div>
  </div>
</body>
</html>

