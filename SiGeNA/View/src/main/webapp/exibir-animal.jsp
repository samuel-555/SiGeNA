<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ include file="/WEB-INF/jspf/permissoes.jspf" %>
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
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    boolean podeGerenciar = temPermissaoGerenciamento(cargo, "animais");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Gestão de Animais</title>
  <link rel="stylesheet" href="CSS\styleanimais.css">
  <link rel="stylesheet" href="CSS\style.css">
  <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
</head>
<body>
<header class="topbar">
            <a href="TarefaController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>

  <div class="container">
    <h1>Gestão de Animais</h1>

    <div class="botoes-acoes">
        <a href="AnimalController?acao=listar" class="btn">Voltar</a>
    </div>
 
    <c:if test="${empty animal}">
        <p>Erro: Animal não encontrado</p>
    </c:if>
    <c:if test="${not empty sessionScope.acaoBemSucedida}">
        <p class="sucesso"><c:out value="${sessionScope.acaoBemSucedida}"/></p>
        <c:remove var="acaoBemSucedida" scope="session"/>
    </c:if>
    <c:if test="${not empty animal}">
        <div class="ficha-animal">
            <h3><c:out value="${animal.nome}"/></h3>
            <p><strong>ID: </strong><c:out value="${animal.id}"/></p>
            <p><strong>Espécie: </strong><c:out value="${animal.especieNome}"/></p>
            <p><strong>Data de nascimento: </strong><c:out value="${animal.dataDeNascimentoFormat}"/> (<c:out value="${animal.idade}"/>)</p>
            <p><strong>Sexo: </strong><c:out value="${animal.sexo}"/></p>
            <p><strong>Peso: </strong><c:out value="${animal.peso}"/> Kg</p>
            <p><strong>Hostil: </strong>
                <c:if test="${not animal.hostilidade}">Não</c:if>
                <c:if test="${animal.hostilidade}">Sim</c:if>
            </p>
            <p><strong>Habitat alocado: </strong><c:out value="${animal.habitatNome}"/></p>
        </div>
    </c:if>
    <div class="botoes-acoes">
        <% if (podeGerenciar) { %>
        <a href="AnimalController?acao=editar&id=<c:out value="${animal.id}"/>" class="btn">Editar dados</a>
        <% } %>
    </div>
  </div>
</body>
</html>
