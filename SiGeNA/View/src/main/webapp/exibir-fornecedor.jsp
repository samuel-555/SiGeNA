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
    boolean podeGerenciar = temPermissaoGerenciamento(cargo, "fornecedores");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SiGeNA - Gestão de Fornecedores</title>
  <link rel="stylesheet" href="CSS\styleanimais.css">
  <link rel="stylesheet" href="CSS\style.css">
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
    <h1>Gestão de Fornecedores</h1>

    <div class="botoes-acoes">
        <a href="FornecedorController?acao=listar" class="btn">Voltar</a>
    </div>
 
    <c:if test="${empty fornecedor}">
        <p>Erro: Fornecedor não encontrado</p>
    </c:if>
    <c:if test="${not empty sessionScope.acaoBemSucedida}">
        <p class="sucesso"><c:out value="${sessionScope.acaoBemSucedida}"/></p>
        <c:remove var="acaoBemSucedida" scope="session"/>
    </c:if>
    <c:if test="${not empty fornecedor}">
        <div class="ficha-animal">
            <h3><c:out value="${fornecedor.nome}"/></h3>
            <p><strong>ID: </strong><c:out value="${fornecedor.id}"/></p>
            <p><strong>Email: </strong><c:out value="${fornecedor.email}"/></p>
            <p><strong>Endereço: </strong><c:out value="${fornecedor.endereco}"/></p>
            <p><strong>Tipo: </strong><c:out value="${fornecedor.tipo}"/></p>
            <p><strong>Descrição: </strong><c:out value="${fornecedor.descricao}"/></p>
        </div>
    </c:if>
    <div class="botoes-acoes">
        <% if (podeGerenciar) { %>
        <a href="FornecedorController?acao=editar&id=<c:out value="${fornecedor.id}"/>" class="btn">Editar dados</a>
        <% } %>
    </div>
  </div>
</body>
</html>
