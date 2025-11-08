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
        <a href="AnimalController?acao=listar" class="btn">Voltar</a>
    </div>
 
    <c:if test="${empty animal}">
        <p>Erro: Animal não encontrado</p>
    </c:if>
    
    <c:if test="${not empty animal}">
        <div class="ficha-animal">
            <h3><c:out value="${animal.nome}"/></h3>
            <p><span>ID:</span><c:out value="${animal.id}"/></p>
            <p><span>Data de nascimento:</span><c:out value="${animal.dataDeNascimentoFormat}"/>(<c:out value="${animal.idade}"/>)</p>
            <p><span>Sexo:</span><c:out value="${animal.sexo}"/></p>
            <p><span>Peso:</span><c:out value="${animal.peso}"/></p>
            <p><span>Hostil:</span>
                <c:if test="${not animal.hostil}">Não</c:if>
                <c:if test="${animal.hostil}">Sim</c:if>
            </p>
        </div>
    </c:if>
    
  </div>
</body>
</html>