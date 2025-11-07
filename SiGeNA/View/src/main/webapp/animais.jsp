<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

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
  <title>SiGeNA - Gestão de Animais</title>
  <link rel="stylesheet" href="CSS\styleanimais.css">
  <link rel="stylesheet" href="CSS\style.css">
</head>
<body>
  <header>
    <div class="titulo"><a href="home.jsp">SiGeNA</a></div>
  </header>

  <div class="container">
    <h1>Gestão de Animais</h1>

    <div class="botoes-acoes">
        <a href="cadastrar-animal.jsp" class="btn">Cadastrar Novo Animal</a>
    </div>

    <div class="historico">
      <h2>Lista de Animais</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Espécie</th>
            <th>Idade</th>
            <th>Habitat</th>
            <th>Dieta</th>
            <th>Saúde</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>001</td>
            <td>Simba</td>
            <td>Leão Africano</td>
            <td>4</td>
            <td>Savana 3</td>
            <td>Carnívoro</td>
            <td>Saudável</td>
            <td>
              <button class="btn-pequeno editar">Editar</button>
              <button class="btn-pequeno excluir">Remover</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
