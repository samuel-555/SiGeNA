<%-- 
    Document   : cadastrar-tarefa
    Created on : 17 de nov. de 2025, 10:06:39
    Author     : aluno
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@taglib uri="jakarta.tags.xml" prefix="x" %>
<%@taglib uri="jakarta.tags.sql" prefix="sql"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Cadastrar Tarefa</title>
  <link rel="stylesheet" href="CSS/style.css">
  <link rel="stylesheet" href="CSS/stylehabitat.css">
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
    <h1>Cadastrar Tarefa</h1>

    <div class="botoes-acoes">
      <a href="HomeController" class="btn">Voltar</a>
    </div>
    
    <div class="formulario">
      <h2>Cadastrar Nova Tarefa</h2>
      
    <form action="${pageContext.request.contextPath}/HomeController" method="post">
    <input type="hidden" name="acao" value="inserir">
        
        <c:if test="${not empty msgErro}">
            <p style="color:red;font-weight:bold">${msgErro}</p>
        </c:if>
        
        <label for="nome">Nome da tarefa:</label>
        <input type="text" id="nome" name="nome" placeholder="Ex: Limpar aquário">
        
        <label for="texto">Descrição da tarefa:</label>
        <input type="text" id="texto" name="texto" placeholder="Ex: Limpar os aquários dos pinguins">
        
        <label for="destinatario">Funcionario encarregado:</label>
            <select name="destinatario" id="destinatario">

                <c:forEach items="${funcionarios}" var="funcionario">
                    <c:if test="${funcionario.estado.name() eq 'ATIVO'}">
                        <option value="${funcionario.id}">${funcionario.nome}: ${funcionario.cargo.name()}</option>
                    </c:if>
                </c:forEach>

            </select>


        <label for="data-conclusao">Data para conclusão:</label>
        <input type="datetime-local" name="data-conclusao" id="data-conclusao"placeholder="Ex: 10/10/2007">
        
        
        <button type="submit" class="btn-enviar">Enviar tarefa</button>
      </form>
    </div>
    </div>
 
</body>
</html>

